// ffff00009070fb40  size=260  FUN_ffff00009070fb40

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009070fb40(void)

{
  undefined8 uVar1;
  
  FUN_ffff00009077de54
            (s__s__d__call_udc_init____USB_VID__ffff0000907d3e68,s_usb_init_ffff0000907fca47,0xb7,
             DAT_ffff000090816da8,DAT_ffff000090816daa);
  DAT_ffff000090816dc0 = FUN_ffff00009070aeb4();
  FUN_ffff00009078b164(&DAT_ffff000090816da8);
  FUN_ffff000090717388(0xffff00009083e0c0,0,1);
  FUN_ffff000090717388(0xffff00009083e080,0,1);
  _DAT_ffff00009083e0b8 = FUN_ffff00009078b430(0x82,0x200);
  if (_DAT_ffff00009083e0b8 != 0) {
    _DAT_ffff00009083e078 = FUN_ffff00009078b430(2,0x200);
    if (_DAT_ffff00009083e078 != 0) {
      _DAT_ffff00009083e0f0 = _DAT_ffff00009083e0b8;
      _DAT_ffff00009083e0f8 = _DAT_ffff00009083e078;
      _DAT_ffff00009083e070 = FUN_ffff00009078b658();
      if (_DAT_ffff00009083e070 != 0) {
        uVar1 = FUN_ffff00009078b764(&PTR_FUN_ffff000090816dc8);
        if ((int)uVar1 == 0) {
          return uVar1;
        }
        thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009083e070);
      }
      FUN_ffff00009078b63c(_DAT_ffff00009083e078);
    }
    FUN_ffff00009078b63c(_DAT_ffff00009083e0b8);
  }
  return 0xffffffff;
}


