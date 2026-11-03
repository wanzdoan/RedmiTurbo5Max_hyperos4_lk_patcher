
int FUN_ffff0000be05cfc4(int param_1,uint param_2,long *param_3,long *param_4)

{
  long *plVar1;
  long *plVar2;
  ulong uVar3;
  byte *pbVar4;
  int iVar5;
  int iVar6;
  undefined **ppuVar7;
  ulong uVar8;
  long lVar9;
  long lVar10;
  ulong uVar11;
  long lVar12;
  undefined *puVar13;
  ulong uVar14;
  ulong uVar15;
  uint uVar16;
  undefined *puVar17;
  ulong uVar18;
  
  if (param_1 != 0) {
    FUN_ffff0000be01a730(&LAB_ffff0000be0ef258,0xffffffff);
    if ((undefined **)PTR_LOOP_ffff0000be0ef250 != &PTR_LOOP_ffff0000be0ef248) {
      param_2 = param_2 & 0xff;
      if (param_2 < 0xd) {
        param_2 = 0xc;
      }
      uVar8 = -1L << ((ulong)param_2 & 0x3f);
      uVar11 = -1L << ((ulong)(param_2 - 0xc) & 0x3f);
      ppuVar7 = (undefined **)PTR_LOOP_ffff0000be0ef250;
      do {
        if (((ulong)ppuVar7[3] & 1) != 0) {
          puVar13 = ppuVar7[4];
          puVar17 = (undefined *)((ulong)(puVar13 + ~uVar8) & uVar8);
          if ((puVar13 <= puVar17) && (puVar17 <= puVar13 + (long)ppuVar7[5] + -1)) {
            uVar14 = (ulong)ppuVar7[5] >> 0xc;
            uVar3 = (ulong)((long)puVar17 - (long)puVar13) >> 0xc & 0xffffffff;
            if (uVar3 < uVar14) {
              uVar15 = (ulong)((long)puVar17 - (long)puVar13) >> 0xc;
              if ((uint)((int)uVar15 + param_1) <= uVar14) {
                uVar18 = uVar15 & 0xffffffff;
                do {
                  pbVar4 = ppuVar7[7] + uVar3 * 0x18 + 0x10;
                  iVar6 = param_1;
                  iVar5 = (int)uVar15;
                  while( true ) {
                    iVar5 = iVar5 + -1;
                    uVar16 = (uint)uVar18;
                    if ((*pbVar4 & 1) != 0) break;
                    iVar6 = iVar6 + -1;
                    pbVar4 = pbVar4 + 0x18;
                    if (iVar6 == 0) {
                      uVar8 = uVar18 & 0xffffffff;
                      if (uVar16 < uVar16 + param_1) {
                        lVar10 = (uVar16 + param_1) - uVar8;
                        lVar9 = (uVar8 + (uVar18 & 0xffffffff) * 2) * 8;
                        do {
                          plVar1 = (long *)(ppuVar7[7] + lVar9);
                          plVar2 = (long *)plVar1[1];
                          *plVar2 = *plVar1;
                          *(long **)(*plVar1 + 8) = plVar2;
                          *plVar1 = 0;
                          plVar1[1] = 0;
                          *(uint *)(plVar1 + 2) = *(uint *)(plVar1 + 2) | 1;
                          ppuVar7[6] = ppuVar7[6] + -1;
                          if (param_4 != (long *)0x0) {
                            lVar12 = *param_4;
                            plVar1[1] = (long)param_4;
                            *plVar1 = lVar12;
                            lVar12 = *param_4;
                            *param_4 = (long)plVar1;
                            *(long **)(lVar12 + 8) = plVar1;
                          }
                          lVar9 = lVar9 + 0x18;
                          lVar10 = lVar10 + -1;
                        } while (lVar10 != 0);
                      }
                      if (param_3 != (long *)0x0) {
                        *param_3 = (long)(ppuVar7[4] + uVar8 * 0x1000);
                      }
                      FUN_ffff0000be01a7c8(&LAB_ffff0000be0ef258);
                      return param_1;
                    }
                  }
                  uVar18 = ((ulong)(uVar16 - iVar5) + ~uVar11 & uVar11) + uVar15;
                  uVar3 = uVar18 & 0xffffffff;
                } while (uVar3 < uVar14 && (uint)((int)uVar18 + param_1) <= uVar14);
              }
            }
          }
        }
        ppuVar7 = (undefined **)ppuVar7[1];
      } while (ppuVar7 != &PTR_LOOP_ffff0000be0ef248);
    }
    FUN_ffff0000be01a7c8(&LAB_ffff0000be0ef258);
  }
  return 0;
}


