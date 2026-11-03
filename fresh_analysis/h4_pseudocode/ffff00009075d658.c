
undefined8 FUN_ffff00009075d658(void)

{
  undefined8 uVar1;
  
  if ((DAT_ffff00009087bf50 & 1) == 0) {
    FUN_ffff00009077e11c
              (s_skip__s_before_init_ffff0000907d99dd,s_primary_display_suspend_ffff0000907e81f1);
    uVar1 = 0xffffffff;
  }
  else {
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff00009075c2dc(DAT_ffff00009087bf70);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff000090755cdc(DAT_ffff00009087bf88,0);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff0000907563bc(DAT_ffff00009087bf88,0);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    uVar1 = 0;
    DAT_ffff00009087bf60 = 0;
  }
  return uVar1;
}


