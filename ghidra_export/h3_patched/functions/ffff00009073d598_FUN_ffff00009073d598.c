// ffff00009073d598  size=952  FUN_ffff00009073d598

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073d598(long *param_1,int *param_2)

{
  bool bVar1;
  long lVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  undefined8 uVar7;
  char *pcVar8;
  int iVar9;
  ulong uVar10;
  long lVar11;
  uint uVar12;
  long *plVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  uint *puVar17;
  undefined8 local_68;
  
  *param_2 = 0;
  if (_DAT_ffff00009086e0e4 == 0) {
    uVar7 = 0;
  }
  else {
    lVar6 = FUN_ffff00009077d24c(0x4000);
    *param_1 = lVar6;
    uVar7 = 0;
    if (lVar6 != 0) {
      local_68 = 0;
      iVar4 = FUN_ffff00009077f434
                        (lVar6 + *param_2,0x4000 - (long)*param_2,
                         s__________________________s_lastb_ffff000090802679,&DAT_ffff0000907e13ee);
      *param_2 = *param_2 + iVar4;
      uVar12 = _DAT_ffff00009086e0f0;
      uVar15 = (ulong)_DAT_ffff00009086e0f0;
      FUN_ffff00009077e858(&local_68,&DAT_ffff0000907e13f0,4);
      FUN_ffff00009077de54
                (s__s__Chip__s_ffff0000907d1347,s_lastbus_dump_v1_ffff0000907d9347,&local_68);
      pcVar8 = s_False_ffff0000907f142f;
      if (uVar12 != 0) {
        uVar14 = 0;
        uVar12 = 0;
        do {
          lVar2 = uVar14 * 0x80 + -0xffff6f791f08;
          iVar4 = FUN_ffff00009077ede4(&local_68,&DAT_ffff0000907dfa51);
          if ((iVar4 == 0) ||
             (iVar4 = FUN_ffff00009077ede4(&local_68,&DAT_ffff0000907e4ac8), iVar4 == 0)) {
            iVar4 = FUN_ffff00009077ede4(lVar2,s_debug_ctrl_ao_MM_AO_ffff0000907fd494);
            if ((iVar4 != 0) || (2 < _DAT_ffff00001c001ea8 >> 0x1e)) {
              iVar4 = FUN_ffff00009077ede4(lVar2,s_debug_ctrl_ao_MMUP_AO_ffff0000907e4ab2);
              if (iVar4 == 0) {
                uVar10 = 0x4e60;
LAB_ffff00009073d7a0:
                if (*(uint *)(uVar10 | 0xffff00001c000000) >> 0x1e == 0) goto LAB_ffff00009073d6b8;
              }
LAB_ffff00009073d7b4:
              plVar13 = (long *)(uVar14 * 0x80 + -0xffff6f791ee0);
              uVar3 = *(uint *)*plVar13;
              if ((uVar3 & 1) != 0) {
                iVar5 = FUN_ffff00009077f434
                                  (lVar6 + *param_2,0x4000 - (long)*param_2,&DAT_ffff0000907f8254);
                iVar4 = *param_2;
                lVar11 = *plVar13;
                iVar9 = (int)((long)iVar4 + (long)iVar5);
                *param_2 = iVar9;
                puVar17 = (uint *)(uVar14 * 0x80 + -0xffff6f791ed8);
                iVar5 = FUN_ffff00009077f434
                                  (lVar6 + (long)iVar4 + (long)iVar5,(long)(0x4000 - iVar9),
                                   s__s__p__d_ffff0000907dacad,lVar2,lVar11 + 0x1000000000000,
                                   *puVar17);
                iVar4 = *param_2;
                iVar9 = (int)((long)iVar4 + (long)iVar5);
                *param_2 = iVar9;
                iVar4 = FUN_ffff00009077f434
                                  (lVar6 + (long)iVar4 + (long)iVar5,(long)(0x4000 - iVar9),
                                   s_____ffff0000907f1435);
                iVar4 = *param_2 + iVar4;
                *param_2 = iVar4;
                if (*puVar17 != 0) {
                  uVar16 = 0;
                  uVar10 = 0;
                  do {
                    iVar4 = FUN_ffff00009077f434
                                      (lVar6 + iVar4,(long)(0x4000 - iVar4),s__08x_ffff0000907e2e11,
                                       *(undefined4 *)(*plVar13 + (uVar16 & 0xfffffffc) + 0x408));
                    uVar10 = uVar10 + 1;
                    uVar16 = uVar16 + 4;
                    iVar4 = *param_2 + iVar4;
                    *param_2 = iVar4;
                  } while (uVar10 < *puVar17);
                }
                uVar10 = *(ulong *)(*plVar13 + 0x400);
                uVar16 = uVar10;
                if (uVar10 == 0) {
                  uVar10 = 0;
                }
                else {
                  do {
                    uVar10 = uVar16 >> 1 ^ uVar10;
                    bVar1 = 1 < uVar16;
                    uVar16 = uVar16 >> 1;
                  } while (bVar1);
                }
                iVar4 = FUN_ffff00009077f434
                                  (lVar6 + iVar4,(long)(0x4000 - iVar4),
                                   s_timestamp__0x_llx_ffff0000908026b6,uVar10);
                *param_2 = *param_2 + iVar4;
              }
              uVar12 = uVar12 | uVar3 & 1;
            }
          }
          else {
            iVar4 = FUN_ffff00009077ede4(lVar2,s_debug_ctrl_ao_MM_AO_ffff0000907fd494);
            if ((iVar4 != 0) || (2 < _DAT_ffff00001c001ea8 >> 0x1e)) {
              iVar4 = FUN_ffff00009077ede4(lVar2,s_debug_ctrl_ao_MMUP_AO_ffff0000907e4ab2);
              if (iVar4 != 0) goto LAB_ffff00009073d7b4;
              if (_DAT_ffff00001c004e60 >> 0x1e != 0) {
                uVar10 = 0x1ea8;
                goto LAB_ffff00009073d7a0;
              }
            }
          }
LAB_ffff00009073d6b8:
          uVar14 = uVar14 + 1;
        } while (uVar14 != uVar15);
        pcVar8 = &DAT_ffff0000907e62f1;
        if (uVar12 == 0) {
          pcVar8 = s_False_ffff0000907f142f;
        }
      }
      iVar5 = FUN_ffff00009077f434
                        (lVar6 + *param_2,0x4000 - (long)*param_2,
                         s__lastbus_timeout____s_ffff0000907cf8e6,pcVar8);
      iVar4 = *param_2;
      uVar7 = 1;
      *param_2 = iVar4 + iVar5;
      if (0x4000 < iVar4 + iVar5) {
        *param_2 = 0x4000;
      }
    }
  }
  return uVar7;
}


