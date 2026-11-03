
void FUN_ffff0000907ce91c(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff000090741bdc(s_usb2jtag_ffff0000907f5b82);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___USB2JTAG__current_settin_ffff0000907d71a1,s_get_usb2jtag_ffff0000907d565b,
               0x1a,0);
  }
  else {
    FUN_ffff000090741bdc(s_usb2jtag_ffff0000907f5b82);
    iVar1 = FUN_ffff00009077d72c();
    FUN_ffff00009077e11c
              (s__s__d___USB2JTAG__current_settin_ffff0000907d71a1,s_get_usb2jtag_ffff0000907d565b,
               0x1a,iVar1);
    if (iVar1 == 1) {
      pcVar3 = s_usb2jtag_mode_1_ffff0000907d5676;
      goto LAB_ffff0000907ce9a4;
    }
  }
  pcVar3 = s_usb2jtag_mode_0_ffff0000907d23fd;
LAB_ffff0000907ce9a4:
  FUN_ffff000090719078(pcVar3);
  return;
}


