// ffff00009077ad94  size=304  FUN_ffff00009077ad94

uint FUN_ffff00009077ad94(ulong param_1,uint param_2,long *param_3)

{
  long *plVar1;
  uint uVar2;
  undefined **ppuVar3;
  undefined *puVar4;
  uint *puVar5;
  undefined *puVar6;
  long *plVar7;
  ulong uVar8;
  long lVar9;
  uint uVar10;
  uint uVar11;
  
  FUN_ffff000090717738(&LAB_ffff000090832860,0xffffffff);
  if ((undefined **)PTR_LOOP_ffff000090832858 == &PTR_LOOP_ffff000090832850) {
    uVar11 = 0;
  }
  else {
    uVar10 = 0;
    puVar4 = (undefined *)(param_1 & 0xfffffffffffff000);
    ppuVar3 = (undefined **)PTR_LOOP_ffff000090832858;
    do {
      uVar11 = param_2;
      if (uVar10 < param_2) {
        while( true ) {
          puVar6 = ppuVar3[4];
          if ((puVar4 < puVar6) || (puVar6 + (long)ppuVar3[5] + -1 < puVar4)) break;
          uVar8 = (ulong)((long)puVar4 - (long)puVar6) >> 0xc;
          puVar5 = (uint *)(ppuVar3[7] + uVar8 * 0x18 + 0x10);
          if ((*puVar5 & 1) != 0) break;
          plVar7 = (long *)(ppuVar3[7] + uVar8 * 0x18);
          uVar10 = uVar10 + 1;
          puVar4 = puVar4 + 0x1000;
          plVar1 = (long *)plVar7[1];
          *plVar1 = *plVar7;
          lVar9 = *plVar7;
          *plVar7 = 0;
          *(long **)(lVar9 + 8) = plVar1;
          uVar2 = *puVar5;
          plVar7[1] = (long)param_3;
          *puVar5 = uVar2 | 1;
          puVar6 = ppuVar3[6];
          *plVar7 = *param_3;
          lVar9 = *param_3;
          *param_3 = (long)plVar7;
          *(long **)(lVar9 + 8) = plVar7;
          ppuVar3[6] = puVar6 + -1;
          if (param_2 == uVar10) goto code_r0xffff00009077aeac;
        }
      }
    } while ((uVar10 != param_2) &&
            (ppuVar3 = (undefined **)ppuVar3[1], uVar11 = uVar10,
            ppuVar3 != &PTR_LOOP_ffff000090832850));
  }
code_r0xffff00009077aeac:
  FUN_ffff0000907177d0(&LAB_ffff000090832860);
  return uVar11;
}


