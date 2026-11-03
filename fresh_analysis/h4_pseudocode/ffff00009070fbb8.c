
undefined8 FUN_ffff00009070fbb8(void)

{
  undefined8 uVar1;
  
  FUN_ffff00009077e11c
            (s__s__d__call_udc_init____USB_VID__ffff0000907d3ea5,s_usb_init_ffff0000907fcbef,0xb7,
             DAT_ffff000090816db8,DAT_ffff000090816dba);
  DAT_ffff000090816dd0 = FUN_ffff00009070af2c();
  FUN_ffff00009078b424(&DAT_ffff000090816db8);
  FUN_ffff0000907174cc(&DAT_ffff00009083e0f8,0,1);
  FUN_ffff0000907174cc(&DAT_ffff00009083e0b8,0,1);
  DAT_ffff00009083e0f0 = FUN_ffff00009078b6f0(0x82,0x200);
  if (DAT_ffff00009083e0f0 != 0) {
    DAT_ffff00009083e0b0 = FUN_ffff00009078b6f0(2,0x200);
    if (DAT_ffff00009083e0b0 != 0) {
      DAT_ffff00009083e128 = DAT_ffff00009083e0f0;
      DAT_ffff00009083e130 = DAT_ffff00009083e0b0;
      DAT_ffff00009083e0a8 = FUN_ffff00009078b918();
      if (DAT_ffff00009083e0a8 != 0) {
        uVar1 = FUN_ffff00009078ba24(&PTR_LAB_ffff000090816dd8);
        if ((int)uVar1 == 0) {
          return uVar1;
        }
        thunk_FUN_ffff0000907c314c(DAT_ffff00009083e0a8);
      }
      FUN_ffff00009078b8fc(DAT_ffff00009083e0b0);
    }
    FUN_ffff00009078b8fc(DAT_ffff00009083e0f0);
  }
  return 0xffffffff;
}


