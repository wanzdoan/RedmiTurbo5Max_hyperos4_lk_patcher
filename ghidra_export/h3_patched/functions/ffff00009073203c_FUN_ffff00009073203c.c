// ffff00009073203c  size=136  FUN_ffff00009073203c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073203c(void)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 local_18;
  
  local_18 = 0;
  lVar1 = FUN_ffff00009072d9f0(s_ccci_tag_mem_ffff0000907f6106,0x10000,&local_18);
  if (lVar1 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__allocate_tag_memor_ffff0000907ef8b2,
               s_ccci_lk_tag_info_init_ffff0000907fef19,0x3d);
    uVar2 = 0xffffffff;
  }
  else {
    uVar2 = 0;
    _DAT_ffff00009086d9f0 = 0;
    _DAT_ffff00009086d9fc = 0;
    _DAT_ffff00009086d9e8 = local_18;
    _DAT_ffff00009086d9f4 = 0x300000000;
    _DAT_ffff00009086da04 = 0;
    _DAT_ffff00009086da0c = 0;
  }
  return uVar2;
}


