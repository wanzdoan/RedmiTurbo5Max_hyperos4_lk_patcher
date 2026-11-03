// File offset entry: 0xc828

void FUN_ffff0000b800c828(int param_1)

{
  int iVar1;
  long lVar2;
  int iVar3;
  long lVar4;
  long *plVar5;
  undefined8 *puVar6;
  undefined8 *puVar7;
  
  FUN_ffff0000b804beb0
            (s__s__d__mblock_magic_0x_x_mblock__ffff0000b8085f04,
             s_mblock_show_internal_ffff0000b809efa7,0x8d,DAT_ffff0000b8115150,DAT_ffff0000b8115154)
  ;
  if (param_1 != 0) {
    FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  }
  puVar6 = (undefined8 *)(DAT_ffff0000b8114508 + 8);
  if (puVar6 + (ulong)(DAT_ffff0000b8114548 - 1) * 3 < puVar6) {
    lVar2 = 0;
  }
  else {
    lVar2 = 0;
    iVar3 = 0;
    do {
      puVar7 = puVar6 + 3;
      lVar4 = puVar6[1];
      FUN_ffff0000b804beb0
                (s__s__d__mblock__d__>start__0x_llx_ffff0000b80995b9,
                 s_mblock_show_internal_ffff0000b809efa7,0x97,iVar3,*puVar6,lVar4);
      lVar2 = lVar4 + lVar2;
      iVar3 = iVar3 + 1;
      puVar6 = puVar7;
    } while (puVar7 <= (undefined8 *)
                       (DAT_ffff0000b8114508 + (ulong)(DAT_ffff0000b8114548 - 1) * 0x18 + 8));
  }
  if (DAT_ffff0000b8115158 != 0) {
    iVar3 = 0;
    plVar5 = (long *)(DAT_ffff0000b8114508 + 0xc18);
    lVar4 = DAT_ffff0000b8114508;
    iVar1 = DAT_ffff0000b8115158;
    do {
      if ((long *)(lVar4 + 0x5780U) < plVar5) break;
      if (*plVar5 != 0) {
        lVar4 = plVar5[1];
        FUN_ffff0000b804beb0
                  (s__s__d__mblock_R__d__start__0x_ll_ffff0000b80930b2,
                   s_mblock_show_internal_ffff0000b809efa7,0xa4,iVar3,*plVar5,lVar4,(int)plVar5[2],
                   (long)plVar5 + 0x14);
        lVar2 = lVar4 + lVar2;
        lVar4 = DAT_ffff0000b8114508;
        iVar1 = DAT_ffff0000b8115158;
      }
      iVar3 = iVar3 + 1;
      plVar5 = plVar5 + 0x13;
    } while (iVar1 != 0);
  }
  FUN_ffff0000b804beb0
            (s__s__d__Total_DRAM___0x_llx_ffff0000b8086e28,s_mblock_show_internal_ffff0000b809efa7,
             0xa9,lVar2);
  if (param_1 != 0) {
    FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
    return;
  }
  return;
}


