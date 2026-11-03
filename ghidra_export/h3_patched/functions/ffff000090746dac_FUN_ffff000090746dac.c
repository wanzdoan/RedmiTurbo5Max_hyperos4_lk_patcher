// ffff000090746dac  size=92  FUN_ffff000090746dac

uint FUN_ffff000090746dac(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  uint uVar1;
  long lVar2;
  
  lVar2 = FUN_ffff00009076daec();
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s___s___s_is_not_found_ffff0000907e641a,s_connsys_get_fdt_prop_ffff0000907e3064,
               param_3);
    uVar1 = 0;
  }
  else {
    uVar1 = (*(uint *)(lVar2 + 0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar2 + 0xc) & 0xff00ff) << 8;
    uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  return uVar1;
}


