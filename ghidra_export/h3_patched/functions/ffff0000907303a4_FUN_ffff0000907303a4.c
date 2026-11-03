// ffff0000907303a4  size=252  FUN_ffff0000907303a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907303a4(undefined4 param_1)

{
  undefined4 *puVar1;
  uint uVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  
  uVar2 = _DAT_ffff00009086d9ac;
  if ((DAT_ffff00009086d9a8 & 1) == 0) {
    pcVar3 = s__CCCI___s__d__MD_memory_layout_o_ffff0000907f29a3;
    pcVar4 = s_ccci_create_1st_mem_node_ffff0000907d6093;
    uVar5 = 0x191;
  }
  else if (_DAT_ffff00009086d998 == 0) {
    pcVar3 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907ef88d;
    pcVar4 = s_get_free_md_mem_blk_node_ffff0000907e7882;
    uVar5 = 0xba;
  }
  else {
    if (-1 < (int)_DAT_ffff00009086d9ac) {
      if (_DAT_ffff00009086d9b4 <= (int)_DAT_ffff00009086d9ac) {
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2a16,
                   s_get_free_md_mem_blk_node_ffff0000907e7882,0xc3,(ulong)_DAT_ffff00009086d9ac,
                   _DAT_ffff00009086d9b4 + -1);
        return 0xffffffff;
      }
      puVar1 = (undefined4 *)(_DAT_ffff00009086d998 + (ulong)_DAT_ffff00009086d9ac * 0x20);
      _DAT_ffff00009086d9ac = puVar1[6];
      *(undefined8 *)(puVar1 + 2) = 0;
      *puVar1 = 0;
      puVar1[1] = param_1;
      puVar1[6] = 0xffffffff;
      _DAT_ffff00009086d9b0 = uVar2;
      DAT_ffff00009086d9b8 = 1;
      _DAT_ffff00009086d9bc = 1;
      return 0;
    }
    pcVar3 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b3b;
    pcVar4 = s_get_free_md_mem_blk_node_ffff0000907e7882;
    uVar5 = 0xbe;
  }
  FUN_ffff00009072ff90(pcVar3,pcVar4,uVar5);
  return 0xffffffff;
}


