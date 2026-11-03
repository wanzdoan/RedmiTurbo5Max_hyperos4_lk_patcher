
long FUN_ffff0000b8049a60(long *param_1)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  undefined **ppuVar4;
  undefined8 *puVar5;
  undefined *puVar6;
  long lVar7;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b8102038,0xffffffff);
  plVar2 = (long *)param_1[1];
  if (plVar2 == param_1) {
    lVar7 = 0;
  }
  else {
    lVar7 = 0;
    do {
      plVar1 = (long *)plVar2[1];
      *plVar1 = *plVar2;
      lVar3 = *plVar2;
      *plVar2 = 0;
      *(long **)(lVar3 + 8) = plVar1;
      plVar2[1] = 0;
      ppuVar4 = (undefined **)PTR_LOOP_ffff0000b8102030;
      if ((undefined **)PTR_LOOP_ffff0000b8102030 != &PTR_LOOP_ffff0000b8102028) {
        do {
          if ((ppuVar4[7] <= plVar2) &&
             (plVar2 < (long *)((long)ppuVar4[7] + ((ulong)ppuVar4[5] >> 0xc) * 3 * 8))) {
            lVar7 = lVar7 + 1;
            *plVar2 = (long)(ppuVar4 + 8);
            *(uint *)(plVar2 + 2) = *(uint *)(plVar2 + 2) & 0xfffffffe;
            puVar6 = ppuVar4[6];
            plVar2[1] = (long)ppuVar4[9];
            puVar5 = (undefined8 *)ppuVar4[9];
            ppuVar4[9] = (undefined *)plVar2;
            *puVar5 = plVar2;
            ppuVar4[6] = puVar6 + 1;
            break;
          }
          ppuVar4 = (undefined **)ppuVar4[1];
        } while (ppuVar4 != &PTR_LOOP_ffff0000b8102028);
      }
      plVar2 = (long *)param_1[1];
    } while (plVar2 != param_1);
  }
  FUN_ffff0000b800ae28(&LAB_ffff0000b8102038);
  return lVar7;
}


