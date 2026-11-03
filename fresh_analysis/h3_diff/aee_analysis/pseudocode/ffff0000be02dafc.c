
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be02dafc(long *param_1,int *param_2)

{
  bool bVar1;
  undefined *puVar2;
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
  if (DAT_ffff0000be118444 == 0) {
    uVar7 = 0;
  }
  else {
    lVar6 = FUN_ffff0000be05f1bc(0x4000);
    *param_1 = lVar6;
    uVar7 = 0;
    if (lVar6 != 0) {
      local_68 = 0;
      iVar4 = FUN_ffff0000be0611dc
                        (lVar6 + *param_2,0x4000 - (long)*param_2,
                         s__________________________s_lastb_ffff0000be0c63ba,&DAT_ffff0000be0af40e);
      *param_2 = *param_2 + iVar4;
      uVar12 = DAT_ffff0000be118450;
      uVar15 = (ulong)DAT_ffff0000be118450;
      FUN_ffff0000be0607b0(&local_68,&DAT_ffff0000be0af410,4);
      FUN_ffff0000be05fd98
                (s__s__Chip__s_ffff0000be0a4923,s_lastbus_dump_v1_ffff0000be0aa451,&local_68);
      pcVar8 = s_False_ffff0000be0baebe;
      if (uVar12 != 0) {
        uVar14 = 0;
        uVar12 = 0;
        do {
          puVar2 = &DAT_ffff0000be118458 + uVar14 * 0x80;
          iVar4 = FUN_ffff0000be060cb8(&local_68,&DAT_ffff0000be0ae85a);
          if ((iVar4 == 0) ||
             (iVar4 = FUN_ffff0000be060cb8(&local_68,&DAT_ffff0000be0b1f18), iVar4 == 0)) {
            iVar4 = FUN_ffff0000be060cb8(puVar2,s_debug_ctrl_ao_MM_AO_ffff0000be0c2d7d);
            if ((iVar4 != 0) || (2 < _DAT_ffff00001c001ea8 >> 0x1e)) {
              iVar4 = FUN_ffff0000be060cb8(puVar2,s_debug_ctrl_ao_MMUP_AO_ffff0000be0b1f02);
              if (iVar4 == 0) {
                uVar10 = 0x4e60;
LAB_ffff0000be02dd04:
                if (*(uint *)(uVar10 | 0xffff00001c000000) >> 0x1e == 0) goto LAB_ffff0000be02dc1c;
              }
LAB_ffff0000be02dd18:
              plVar13 = &DAT_ffff0000be118480 + uVar14 * 0x10;
              uVar3 = *(uint *)*plVar13;
              if ((uVar3 & 1) != 0) {
                iVar5 = FUN_ffff0000be0611dc
                                  (lVar6 + *param_2,0x4000 - (long)*param_2,&DAT_ffff0000be0bf861);
                iVar4 = *param_2;
                lVar11 = *plVar13;
                iVar9 = (int)((long)iVar4 + (long)iVar5);
                *param_2 = iVar9;
                puVar17 = &DAT_ffff0000be118488 + uVar14 * 0x20;
                iVar5 = FUN_ffff0000be0611dc
                                  (lVar6 + (long)iVar4 + (long)iVar5,(long)(0x4000 - iVar9),
                                   s__s__p__d_ffff0000be0ab6df,puVar2,lVar11 + 0x1000000000000,
                                   *puVar17);
                iVar4 = *param_2;
                iVar9 = (int)((long)iVar4 + (long)iVar5);
                *param_2 = iVar9;
                iVar4 = FUN_ffff0000be0611dc
                                  (lVar6 + (long)iVar4 + (long)iVar5,(long)(0x4000 - iVar9),
                                   s_____ffff0000be0baec4);
                iVar4 = *param_2 + iVar4;
                *param_2 = iVar4;
                if (*puVar17 != 0) {
                  uVar16 = 0;
                  uVar10 = 0;
                  do {
                    iVar4 = FUN_ffff0000be0611dc
                                      (lVar6 + iVar4,(long)(0x4000 - iVar4),s__08x_ffff0000be0b0cdb,
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
                iVar4 = FUN_ffff0000be0611dc
                                  (lVar6 + iVar4,(long)(0x4000 - iVar4),
                                   s_timestamp__0x_llx_ffff0000be0c63f7,uVar10);
                *param_2 = *param_2 + iVar4;
              }
              uVar12 = uVar12 | uVar3 & 1;
            }
          }
          else {
            iVar4 = FUN_ffff0000be060cb8(puVar2,s_debug_ctrl_ao_MM_AO_ffff0000be0c2d7d);
            if ((iVar4 != 0) || (2 < _DAT_ffff00001c001ea8 >> 0x1e)) {
              iVar4 = FUN_ffff0000be060cb8(puVar2,s_debug_ctrl_ao_MMUP_AO_ffff0000be0b1f02);
              if (iVar4 != 0) goto LAB_ffff0000be02dd18;
              if (_DAT_ffff00001c004e60 >> 0x1e != 0) {
                uVar10 = 0x1ea8;
                goto LAB_ffff0000be02dd04;
              }
            }
          }
LAB_ffff0000be02dc1c:
          uVar14 = uVar14 + 1;
        } while (uVar14 != uVar15);
        pcVar8 = &DAT_ffff0000be0b300c;
        if (uVar12 == 0) {
          pcVar8 = s_False_ffff0000be0baebe;
        }
      }
      iVar5 = FUN_ffff0000be0611dc
                        (lVar6 + *param_2,0x4000 - (long)*param_2,
                         s__lastbus_timeout____s_ffff0000be0a3521,pcVar8);
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


