// ffff0000907ce2a8  size=152  FUN_ffff0000907ce2a8

void FUN_ffff0000907ce2a8(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff000090741944(s_usb2jtag_ffff0000907f5a0b);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d___USB2JTAG__current_settin_ffff0000907d710b,s_get_usb2jtag_ffff0000907d561e,
               0x1a,0);
  }
  else {
    FUN_ffff000090741944(s_usb2jtag_ffff0000907f5a0b);
    iVar1 = FUN_ffff00009077d464();
    FUN_ffff00009077de54
              (s__s__d___USB2JTAG__current_settin_ffff0000907d710b,s_get_usb2jtag_ffff0000907d561e,
               0x1a,iVar1);
    if (iVar1 == 1) {
      pcVar3 = s_usb2jtag_mode_1_ffff0000907d5639;
      goto LAB_ffff0000907ce330;
    }
  }
  pcVar3 = s_usb2jtag_mode_0_ffff0000907d23c0;
LAB_ffff0000907ce330:
  FUN_ffff000090718f34(pcVar3);
  return;
}


