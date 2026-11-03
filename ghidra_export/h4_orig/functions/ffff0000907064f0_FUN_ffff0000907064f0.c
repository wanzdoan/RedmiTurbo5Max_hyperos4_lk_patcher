// ffff0000907064f0  size=220  FUN_ffff0000907064f0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907064f0(ulong param_1)

{
  undefined1 *puVar1;
  undefined1 local_30 [4];
  undefined1 local_2c [4];
  undefined1 local_28 [4];
  undefined1 local_24 [4];
  
  if (_DAT_ffff00009083d078 == 0) {
    _DAT_ffff00009083d078 = FUN_ffff000090740610(1,4);
    if (_DAT_ffff00009083d078 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__Failed_to_get_MT6363_spmi_ffff0000908002fc,
                 s_mt6363_enable_dummy_load_ffff0000907d8b56,0xf0);
      return;
    }
  }
  if ((param_1 & 1) == 0) {
    local_28[0] = 0;
    FUN_ffff0000907404b4(_DAT_ffff00009083d078,0x220b,local_28,1);
    puVar1 = local_24;
    local_24[0] = 0;
  }
  else {
    local_30[0] = 0xff;
    FUN_ffff0000907404b4(_DAT_ffff00009083d078,0x220b,local_30,1);
    puVar1 = local_2c;
    local_2c[0] = 0xf0;
  }
  FUN_ffff0000907404b4(_DAT_ffff00009083d078,0x220c,puVar1,1);
  return;
}


