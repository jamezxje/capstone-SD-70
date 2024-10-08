(function ($) {
  'use strict';
  $(function () {

    if ($("#marketingOverview").length) {
      const marketingOverviewCanvas = document.getElementById('marketingOverview');
      Chart.defaults.set('plugins.datalabels', {
        color: '#FE777B'
      });
      var chart = new Chart(marketingOverviewCanvas, {
        type: 'bar',
        data: {
          labels: ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"],
          datasets: [{
            label: 'Last week',
            data: [110, 220, 200, 190, 220, 110, 210, 110, 205, 202, 201, 150],
            backgroundColor: "#52CDFF",
            borderColor: [
              '#52CDFF',
            ],
            borderWidth: 0,
            barPercentage: 0.35,
            fill: true, // 3: no fill

          }, {
            label: 'This week',
            data: [215, 290, 210, 250, 290, 230, 290, 210, 280, 220, 190, 300],
            backgroundColor: "#1F3BB3",
            borderColor: [
              '#1F3BB3',
            ],
            borderWidth: 0,
            barPercentage: 0.35,
            fill: true, // 3: no fill
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          elements: {
            line: {
              tension: 0.4,
            }
          },

          scales: {
            y: {
              border: {
                display: false
              },
              grid: {
                display: true,
                drawTicks: false,
                color: "#F0F0F0",
                zeroLineColor: '#F0F0F0',
              },
              ticks: {
                beginAtZero: false,
                autoSkip: true,
                maxTicksLimit: 4,
                color: "#6B778C",
                font: {
                  size: 10,
                }
              }
            },
            x: {
              border: {
                display: false
              },
              stacked: true,
              grid: {
                display: false,
                drawTicks: false,
              },
              ticks: {
                beginAtZero: false,
                autoSkip: true,
                maxTicksLimit: 7,
                color: "#6B778C",
                font: {
                  size: 10,
                }
              }
            }
          },
          plugins: {
            legend: {
              display: false,
            },
            datalabels: {
              color :'white'
            }
          },
        },
        plugins:[
          {
            afterDatasetUpdate: function (chart, args, options) {
              const chartId = chart.canvas.id;
              var i;
              const legendId = `${chartId}-legend`;
              const ul = document.createElement('ul');
              for (i = 0; i < chart.data.datasets.length; i++) {
                ul.innerHTML += `
                  <li>
                    <span style="background-color: ${chart.data.datasets[i].borderColor}"></span>
                    ${chart.data.datasets[i].label}
                  </li>
                `;
              }
              return document.getElementById(legendId).appendChild(ul);
            },
            afterDraw: function (chart){
              const ctx = chart.ctx;
              chart.data.datasets.forEach((dataset, i)=>{
                const meta = chart.getDatasetMeta(i);
                if(!meta.hidden){
                  meta.data.forEach((element, index)=>{
                    const data = dataset.data[index];
                    let total;
                    if(dataset.label === 'This week'){
                      total = chart.data.datasets[0].data[index] + chart.data.datasets[1].data[index]
                    }
                    if (typeof total !== 'undefined') {
                      ctx.fillStyle = '#000';
                      ctx.textAlign = 'center';
                      ctx.textBaseline = 'bottom';
                      ctx.fillText(total, element.x, element.y - 5);
                    }
                  });
                }
              })
            }
          }
        ]
      });
    }

    if ($.cookie('staradmin2-pro-banner') != "true") {
      document.querySelector('#proBanner').classList.add('d-flex');
      document.querySelector('.navbar').classList.remove('fixed-top');
    } else {
      document.querySelector('#proBanner').classList.add('d-none');
      document.querySelector('.navbar').classList.add('fixed-top');
    }

    if ($(".navbar").hasClass("fixed-top")) {
      document.querySelector('.page-body-wrapper').classList.remove('pt-0');
      document.querySelector('.navbar').classList.remove('pt-5');
    } else {
      document.querySelector('.page-body-wrapper').classList.add('pt-0');
      document.querySelector('.navbar').classList.add('pt-5');
      document.querySelector('.navbar').classList.add('mt-3');

    }
    document.querySelector('#bannerClose').addEventListener('click', function () {
      document.querySelector('#proBanner').classList.add('d-none');
      document.querySelector('#proBanner').classList.remove('d-flex');
      document.querySelector('.navbar').classList.remove('pt-5');
      document.querySelector('.navbar').classList.add('fixed-top');
      document.querySelector('.page-body-wrapper').classList.add('proBanner-padding-top');
      document.querySelector('.navbar').classList.remove('mt-3');
      var date = new Date();
      date.setTime(date.getTime() + 24 * 60 * 60 * 1000);
      $.cookie('staradmin2-pro-banner', "true", {expires: date});
    });

  });
  // iconify.load('icons.svg').then(function() {
  //   iconify(document.querySelector('.my-cool.icon'));
  // });

})(jQuery);

