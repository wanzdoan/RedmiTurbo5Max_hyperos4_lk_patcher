// ffff000090781834  size=32  FUN_ffff000090781834

undefined4 FUN_ffff000090781834(ulong param_1)

{
  return *(undefined4 *)(&DAT_ffff00009087cfec + (param_1 & 0xffffffff) * 0x80);
}


