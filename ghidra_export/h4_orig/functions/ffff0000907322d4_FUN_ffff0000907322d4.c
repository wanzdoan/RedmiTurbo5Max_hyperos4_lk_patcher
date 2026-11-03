// ffff0000907322d4  size=136  FUN_ffff0000907322d4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907322d4(void)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 local_18;
  
  local_18 = 0;
  lVar1 = FUN_ffff00009072dc88(s_ccci_tag_mem_ffff0000907f62a7,0x10000,&local_18);
  if (lVar1 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__allocate_tag_memor_ffff0000907efa3a,
               s_ccci_lk_tag_info_init_ffff0000907ff0c1,0x3d);
    uVar2 = 0xffffffff;
  }
  else {
    uVar2 = 0;
    _DAT_ffff00009086da88 = 0;
    _DAT_ffff00009086da94 = 0;
    _DAT_ffff00009086da80 = local_18;
    _DAT_ffff00009086da8c = 0x300000000;
    _DAT_ffff00009086da9c = 0;
    _DAT_ffff00009086daa4 = 0;
  }
  return uVar2;
}


