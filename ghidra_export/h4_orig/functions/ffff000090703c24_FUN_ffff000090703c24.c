// ffff000090703c24  size=300  FUN_ffff000090703c24

void FUN_ffff000090703c24(void)

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
    FUN_ffff00009077ee50(&local_60,0,0x40);
    FUN_ffff00009077f6fc
              (&local_60,0x3f,s_earlycon_uart8250_mmio32_0x_x_ffff0000907ef273,DAT_ffff00009081625c)
    ;
    FUN_ffff000090719078(&local_60);
    FUN_ffff00009077ee50(&local_60,0,0x40);
    uVar1 = FUN_ffff000090742bd4(DAT_ffff00009081625c);
    FUN_ffff00009077f6fc
              (&local_60,0x3f,s_console_ttyS_d__dn1_ffff0000907d3d13,uVar1,DAT_ffff000090816260);
    FUN_ffff000090719078(&local_60);
    FUN_ffff00009077ee50(&local_60,0,0x40);
    FUN_ffff00009077f6fc(&local_60,0x3f,s_log_buf_len__s_ffff0000907d7251,&DAT_ffff0000907faf5d);
    FUN_ffff000090719078(&local_60);
    FUN_ffff000090719078(s_mtk_printk_ctrl_disable_uart_0_ffff0000907d7260);
    FUN_ffff000090719078(s_et7480_disable_uart_0_ffff0000907f0bcc);
    return;
  }
  FUN_ffff000090719078(s_mtk_printk_ctrl_disable_uart_1_ffff0000907faf60);
  FUN_ffff000090719078(s_et7480_disable_uart_1_ffff0000907e5a9c);
  return;
}


