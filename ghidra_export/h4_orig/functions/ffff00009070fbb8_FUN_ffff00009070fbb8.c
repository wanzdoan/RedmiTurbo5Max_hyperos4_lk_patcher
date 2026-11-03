// ffff00009070fbb8  size=260  FUN_ffff00009070fbb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009070fbb8(void)

{
  undefined8 uVar1;
  
  FUN_ffff00009077e11c
            (s__s__d__call_udc_init____USB_VID__ffff0000907d3ea5,s_usb_init_ffff0000907fcbef,0xb7,
             DAT_ffff000090816db8,DAT_ffff000090816dba);
  DAT_ffff000090816dd0 = FUN_ffff00009070af2c();
  FUN_ffff00009078b424(&DAT_ffff000090816db8);
  FUN_ffff0000907174cc(0xffff00009083e0f8,0,1);
  FUN_ffff0000907174cc(0xffff00009083e0b8,0,1);
  _DAT_ffff00009083e0f0 = FUN_ffff00009078b6f0(0x82,0x200);
  if (_DAT_ffff00009083e0f0 != 0) {
    _DAT_ffff00009083e0b0 = FUN_ffff00009078b6f0(2,0x200);
    if (_DAT_ffff00009083e0b0 != 0) {
      _DAT_ffff00009083e128 = _DAT_ffff00009083e0f0;
      _DAT_ffff00009083e130 = _DAT_ffff00009083e0b0;
      _DAT_ffff00009083e0a8 = FUN_ffff00009078b918();
      if (_DAT_ffff00009083e0a8 != 0) {
        uVar1 = FUN_ffff00009078ba24(&PTR_FUN_ffff000090816dd8);
        if ((int)uVar1 == 0) {
          return uVar1;
        }
        thunk_FUN_ffff0000907c314c(_DAT_ffff00009083e0a8);
      }
      FUN_ffff00009078b8fc(_DAT_ffff00009083e0b0);
    }
    FUN_ffff00009078b8fc(_DAT_ffff00009083e0f0);
  }
  return 0xffffffff;
}


