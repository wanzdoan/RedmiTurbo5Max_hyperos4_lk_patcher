// ffff000090719710  size=460  FUN_ffff000090719710

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090719710(int param_1)

{
  int iVar1;
  long lVar2;
  int iVar3;
  long lVar4;
  long *plVar5;
  undefined8 *puVar6;
  undefined8 *puVar7;
  
  FUN_ffff00009077e11c
            (s__s__d__mblock_magic_0x_x_mblock__ffff0000907d27ce,
             s_mblock_show_internal_ffff00009080065b,0x8d,_DAT_ffff00009085b8d8,
             _DAT_ffff00009085b8dc);
  if (param_1 != 0) {
    FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  }
  puVar6 = (undefined8 *)(_DAT_ffff000090850c90 + 8);
  if (puVar6 + (ulong)(_DAT_ffff00009085acd0 - 1) * 3 < puVar6) {
    lVar2 = 0;
  }
  else {
    lVar2 = 0;
    iVar3 = 0;
    do {
      puVar7 = puVar6 + 3;
      lVar4 = puVar6[1];
      FUN_ffff00009077e11c
                (s__s__d__mblock__d__>start__0x_llx_ffff0000907f5ef0,
                 s_mblock_show_internal_ffff00009080065b,0x97,iVar3,*puVar6,lVar4);
      lVar2 = lVar4 + lVar2;
      iVar3 = iVar3 + 1;
      puVar6 = puVar7;
    } while (puVar7 <= (undefined8 *)
                       (_DAT_ffff000090850c90 + (ulong)(_DAT_ffff00009085acd0 - 1) * 0x18 + 8));
  }
  if (_DAT_ffff00009085b8e0 != 0) {
    iVar3 = 0;
    plVar5 = (long *)(_DAT_ffff000090850c90 + 0xc18);
    lVar4 = _DAT_ffff000090850c90;
    iVar1 = _DAT_ffff00009085b8e0;
    do {
      if ((long *)(lVar4 + 0x5780U) < plVar5) break;
      if (*plVar5 != 0) {
        lVar4 = plVar5[1];
        FUN_ffff00009077e11c
                  (s__s__d__mblock_R__d__start__0x_ll_ffff0000907ea8a8,
                   s_mblock_show_internal_ffff00009080065b,0xa4,iVar3,*plVar5,lVar4,(int)plVar5[2],
                   (long)plVar5 + 0x14);
        lVar2 = lVar4 + lVar2;
        lVar4 = _DAT_ffff000090850c90;
        iVar1 = _DAT_ffff00009085b8e0;
      }
      iVar3 = iVar3 + 1;
      plVar5 = plVar5 + 0x13;
    } while (iVar1 != 0);
  }
  FUN_ffff00009077e11c
            (s__s__d__Total_DRAM___0x_llx_ffff0000907d403b,s_mblock_show_internal_ffff00009080065b,
             0xa9,lVar2);
  if (param_1 != 0) {
    FUN_ffff0000907177d0(&LAB_ffff000090818208);
    return;
  }
  return;
}


