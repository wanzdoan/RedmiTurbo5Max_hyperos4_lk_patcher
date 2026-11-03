// ffff00009073142c  size=1020  FUN_ffff00009073142c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009073142c(long param_1,uint param_2)

{
  uint uVar1;
  bool bVar2;
  uint uVar3;
  int iVar4;
  char *pcVar5;
  undefined8 uVar6;
  ulong uVar7;
  uint *puVar8;
  undefined8 *puVar9;
  byte *pbVar10;
  int iVar11;
  long lVar12;
  uint uVar13;
  ulong uVar14;
  ulong uVar15;
  uint uVar16;
  
  uVar14 = (ulong)param_2;
  if (param_2 != 0) {
    uVar7 = 0;
    uVar16 = 0;
    uVar15 = (ulong)param_2;
    pbVar10 = (byte *)(param_1 + 0x20);
    do {
      uVar3 = *(uint *)(pbVar10 + -0x14);
      if ((uVar3 >> 3 & 1) == 0) {
        if ((uVar3 >> 2 & 1) != 0) {
          if ((uVar7 < param_2 - 1) && ((pbVar10[-0x18] & *pbVar10) != 0)) {
            uVar3 = uVar3 | 8;
            uVar16 = uVar16 + 1;
          }
          else {
            uVar3 = uVar3 | 0x10;
          }
          *(uint *)(pbVar10 + -0x14) = uVar3;
        }
      }
      else {
        uVar16 = uVar16 + 1;
      }
      uVar7 = uVar7 + 1;
      pbVar10 = pbVar10 + 0x18;
    } while (uVar15 != uVar7);
    if (uVar16 != 0) {
      uVar3 = FUN_ffff000090798300(0xffff00009086d9c0,8);
      _DAT_ffff00009086d9e0 = uVar3;
      if (uVar3 < uVar16) {
        if (uVar3 != 0) {
          uVar16 = 0;
          do {
            if (param_2 != 0) {
              uVar7 = 0;
              uVar13 = 0;
              puVar8 = (uint *)(param_1 + 0xc);
              iVar4 = -1;
              do {
                iVar11 = iVar4;
                if ((*puVar8 & 0x18) == 8) {
                  uVar1 = puVar8[-2];
                  bVar2 = uVar1 <= uVar13;
                  if (uVar13 <= uVar1) {
                    uVar13 = uVar1;
                  }
                  iVar11 = (int)uVar7;
                  if (bVar2) {
                    iVar11 = iVar4;
                  }
                }
                uVar7 = uVar7 + 1;
                puVar8 = puVar8 + 6;
                iVar4 = iVar11;
              } while (uVar15 != uVar7);
              if (uVar13 != 0) {
                lVar12 = param_1 + (long)iVar11 * 0x18;
                *(uint *)(lVar12 + 0xc) = *(uint *)(lVar12 + 0xc) | 0x10;
              }
            }
            uVar16 = uVar16 + 1;
          } while (uVar16 != uVar3);
        }
      }
      else if (param_2 != 0) {
        puVar8 = (uint *)(param_1 + 0xc);
        do {
          if ((*puVar8 >> 3 & 1) != 0) {
            *puVar8 = *puVar8 | 0x10;
          }
          uVar15 = uVar15 - 1;
          puVar8 = puVar8 + 6;
        } while (uVar15 != 0);
      }
    }
  }
  FUN_ffff000090731034();
  for (iVar4 = FUN_ffff000090731828(0,param_1,uVar14,0,1); 0 < iVar4;
      iVar4 = FUN_ffff000090731828(0,param_1,param_2,iVar4,0)) {
  }
  for (iVar4 = FUN_ffff000090731828(1,param_1,param_2,0,1); 0 < iVar4;
      iVar4 = FUN_ffff000090731828(1,param_1,param_2,iVar4,0)) {
  }
  for (iVar4 = FUN_ffff000090731828(2,param_1,param_2,0,1); 0 < iVar4;
      iVar4 = FUN_ffff000090731828(2,param_1,param_2,iVar4,0)) {
  }
  for (iVar4 = FUN_ffff000090731828(3,param_1,param_2,0,1); 0 < iVar4;
      iVar4 = FUN_ffff000090731828(3,param_1,param_2,iVar4,0)) {
  }
  if (0 < (int)param_2) {
    uVar7 = 0;
    bVar2 = false;
    lVar12 = param_1 + 8;
    do {
      if ((*(uint *)(lVar12 + 4) >> 1 & 1) != 0) {
        if ((*(uint *)(lVar12 + 4) >> 8 & 1) != 0) {
          FUN_ffff00009077de54
                    (s__s__d___CCCI_DSP_is_optional__sk_ffff0000907d44a3,
                     s_apply_dsp_region_mpu_ffff0000907fd1e6,0xea);
          goto LAB_ffff0000907317b0;
        }
        bVar2 = true;
      }
      if ((*(byte *)(lVar12 + 1) & 1) != 0) {
        FUN_ffff00009079835c(4,*(undefined8 *)(lVar12 + 8),*(undefined4 *)(lVar12 + -4),0xffffffff);
        if ((int)param_2 <= (int)uVar7) goto LAB_ffff00009073174c;
        goto LAB_ffff000090731734;
      }
      uVar7 = uVar7 + 1;
      lVar12 = lVar12 + 0x18;
    } while (uVar14 != uVar7);
    if (bVar2) {
      pcVar5 = s__CCCI___s__d__Find_DSP_RO_region_ffff0000907ec34c;
      uVar6 = 0xf3;
      goto LAB_ffff000090731760;
    }
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d__No_DSP_RO_region_ffff0000907feed4,
             s_apply_dsp_region_mpu_ffff0000907fd1e6,0xf6);
LAB_ffff0000907317b0:
  if (0 < (int)param_2) {
    puVar9 = (undefined8 *)(param_1 + 0x10);
    do {
      if ((*(byte *)((long)puVar9 + -4) >> 6 & 1) != 0) {
        iVar4 = FUN_ffff00009079835c(6,*puVar9,*(undefined4 *)((long)puVar9 + -0xc),0xffffffff);
        return iVar4 >> 0x1f;
      }
      puVar9 = puVar9 + 3;
      uVar14 = uVar14 - 1;
    } while (uVar14 != 0);
  }
  FUN_ffff00009077de54
            (s__s__d___CCCI_Skep_DRDI_region_cf_ffff0000907ddfaf,
             s_apply_drdi_region_mpu_ffff0000907e6075,0x111);
  return 0;
  while( true ) {
    lVar12 = lVar12 + 0x18;
    uVar7 = uVar7 + 1;
    if (uVar14 == uVar7) break;
LAB_ffff000090731734:
    if ((*(byte *)(lVar12 + 1) >> 1 & 1) != 0) {
      lVar12 = param_1 + (uVar7 & 0xffffffff) * 0x18;
      iVar4 = FUN_ffff00009079835c
                        (5,*(undefined8 *)(lVar12 + 0x10),*(undefined4 *)(lVar12 + 4),0xffffffff);
      if (iVar4 < 0) {
        return -1;
      }
      goto LAB_ffff0000907317b0;
    }
  }
LAB_ffff00009073174c:
  pcVar5 = s__CCCI___s__d__Find_DSP_RW_region_ffff0000907e118d;
  uVar6 = 0x101;
LAB_ffff000090731760:
  FUN_ffff00009072ff90(pcVar5,s_apply_dsp_region_mpu_ffff0000907fd1e6,uVar6);
  return -1;
}


