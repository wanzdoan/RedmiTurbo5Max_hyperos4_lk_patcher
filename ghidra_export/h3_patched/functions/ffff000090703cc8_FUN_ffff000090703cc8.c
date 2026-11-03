// ffff000090703cc8  size=300  FUN_ffff000090703cc8

void FUN_ffff000090703cc8(void)

{
  undefined4 uVar1;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (DAT_ffff000090816268 != 0) {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    FUN_ffff00009077eb88(&local_60,0,0x40);
    FUN_ffff00009077f434
              (&local_60,0x3f,s_earlycon_uart8250_mmio32_0x_x_ffff0000907ef0eb,DAT_ffff00009081625c)
    ;
    FUN_ffff000090718f34(&local_60);
    FUN_ffff00009077eb88(&local_60,0,0x40);
    uVar1 = FUN_ffff00009074293c(DAT_ffff00009081625c);
    FUN_ffff00009077f434
              (&local_60,0x3f,s_console_ttyS_d__dn1_ffff0000907d3cd6,uVar1,DAT_ffff000090816260);
    FUN_ffff000090718f34(&local_60);
    FUN_ffff00009077eb88(&local_60,0,0x40);
    FUN_ffff00009077f434(&local_60,0x3f,s_log_buf_len__s_ffff0000907d71bb,&DAT_ffff0000907fadc1);
    FUN_ffff000090718f34(&local_60);
    FUN_ffff000090718f34(s_mtk_printk_ctrl_disable_uart_0_ffff0000907d71ca);
    FUN_ffff000090718f34(s_et7480_disable_uart_0_ffff0000907f0a44);
    return;
  }
  FUN_ffff000090718f34(s_mtk_printk_ctrl_disable_uart_1_ffff0000907fadc4);
  FUN_ffff000090718f34(s_et7480_disable_uart_1_ffff0000907e5981);
  return;
}


