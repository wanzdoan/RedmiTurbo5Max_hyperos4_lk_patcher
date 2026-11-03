// ffff0000907ce170  size=108  FUN_ffff0000907ce170

undefined4 FUN_ffff0000907ce170(void)

{
  undefined4 uVar1;
  long lVar2;
  
  lVar2 = FUN_ffff000090741944(s_usb2jtag_ffff0000907f59f4);
  if (lVar2 == 0) {
    uVar1 = 0;
  }
  else {
    FUN_ffff000090741944(s_usb2jtag_ffff0000907f59f4);
    uVar1 = FUN_ffff00009077d464();
  }
  FUN_ffff00009077de54
            (s__s__d___USB2JTAG__current_settin_ffff0000907d710b,s_get_usb2jtag_ffff0000907d561e,
             0x1a,uVar1);
  return uVar1;
}


