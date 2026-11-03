
void FUN_ffff0000be04bdb0(void)

{
  long lVar1;
  ulong uVar2;
  int iVar3;
  int iVar4;
  ulong uVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  undefined4 *puVar9;
  
  uVar7 = 0;
  puVar9 = &DAT_ffff0000be0de270;
  do {
    if ((&DAT_ffff0000be0de290)[uVar7 * 0x14] != 0) {
      uVar6 = (&DAT_ffff0000be0de294)[uVar7 * 0x14];
      uVar5 = (ulong)uVar6;
      if (uVar6 != 0) {
        uVar8 = 0;
        do {
          if ((&DAT_ffff0000be0de290)[uVar7 * 0x14] != 0) {
            uVar5 = (ulong)(uint)puVar9[uVar8];
            lVar1 = (&DAT_ffff0000be0de2a0)[uVar7 * 10] +
                    (ulong)(uint)(&DAT_ffff0000be0de298)[uVar7 * 0x14];
            switch(uVar8 & 0xffffffff) {
            case 0:
              if ((uint *)(lVar1 + 0x70) == (uint *)0x0) {
                iVar4 = 0;
              }
              else {
                iVar3 = (&DAT_ffff0000be126150)[uVar5];
                uVar6 = *(uint *)(lVar1 + 0x70) & 0xffff;
                uVar2 = (long)iVar3 * (long)(int)uVar6 + 0x3fff;
                if (-1 < (long)iVar3 * (long)(int)uVar6) {
                  uVar2 = (long)iVar3 * (long)(int)uVar6;
                }
                iVar4 = 0x65ff;
                if (uVar6 != 0) {
                  iVar4 = (DAT_ffff0000be1260dc * 500 - iVar3) + (int)(uVar2 >> 0xe);
                }
                *(uint *)(&DAT_ffff0000be126118 + uVar5 * 4) = uVar6;
              }
              (&DAT_ffff0000be1260e0)[uVar5] = iVar4;
              uVar6 = *(uint *)(lVar1 + 0xd4);
              break;
            case 1:
              if ((uint *)(lVar1 + 0x74) == (uint *)0x0) {
                iVar4 = 0;
              }
              else {
                iVar3 = (&DAT_ffff0000be126150)[uVar5];
                uVar6 = *(uint *)(lVar1 + 0x74) & 0xffff;
                uVar2 = (long)iVar3 * (long)(int)uVar6 + 0x3fff;
                if (-1 < (long)iVar3 * (long)(int)uVar6) {
                  uVar2 = (long)iVar3 * (long)(int)uVar6;
                }
                iVar4 = 0x65ff;
                if (uVar6 != 0) {
                  iVar4 = (DAT_ffff0000be1260dc * 500 - iVar3) + (int)(uVar2 >> 0xe);
                }
                *(uint *)(&DAT_ffff0000be126118 + uVar5 * 4) = uVar6;
              }
              (&DAT_ffff0000be1260e0)[uVar5] = iVar4;
              uVar6 = *(uint *)(lVar1 + 0xd8);
              break;
            case 2:
              if ((uint *)(lVar1 + 0x78) == (uint *)0x0) {
                iVar4 = 0;
              }
              else {
                iVar3 = (&DAT_ffff0000be126150)[uVar5];
                uVar6 = *(uint *)(lVar1 + 0x78) & 0xffff;
                uVar2 = (long)iVar3 * (long)(int)uVar6 + 0x3fff;
                if (-1 < (long)iVar3 * (long)(int)uVar6) {
                  uVar2 = (long)iVar3 * (long)(int)uVar6;
                }
                iVar4 = 0x65ff;
                if (uVar6 != 0) {
                  iVar4 = (DAT_ffff0000be1260dc * 500 - iVar3) + (int)(uVar2 >> 0xe);
                }
                *(uint *)(&DAT_ffff0000be126118 + uVar5 * 4) = uVar6;
              }
              (&DAT_ffff0000be1260e0)[uVar5] = iVar4;
              uVar6 = *(uint *)(lVar1 + 0xdc);
              break;
            case 3:
              if ((uint *)(lVar1 + 0x7c) == (uint *)0x0) {
                iVar4 = 0;
              }
              else {
                iVar3 = (&DAT_ffff0000be126150)[uVar5];
                uVar6 = *(uint *)(lVar1 + 0x7c) & 0xffff;
                uVar2 = (long)iVar3 * (long)(int)uVar6 + 0x3fff;
                if (-1 < (long)iVar3 * (long)(int)uVar6) {
                  uVar2 = (long)iVar3 * (long)(int)uVar6;
                }
                iVar4 = 0x65ff;
                if (uVar6 != 0) {
                  iVar4 = (DAT_ffff0000be1260dc * 500 - iVar3) + (int)(uVar2 >> 0xe);
                }
                *(uint *)(&DAT_ffff0000be126118 + uVar5 * 4) = uVar6;
              }
              (&DAT_ffff0000be1260e0)[uVar5] = iVar4;
              uVar6 = *(uint *)(lVar1 + 0xe0);
              break;
            default:
              if ((uint *)(lVar1 + 0x70) == (uint *)0x0) {
                iVar4 = 0;
              }
              else {
                iVar3 = (&DAT_ffff0000be126150)[uVar5];
                uVar6 = *(uint *)(lVar1 + 0x70) & 0xffff;
                uVar2 = (long)iVar3 * (long)(int)uVar6 + 0x3fff;
                if (-1 < (long)iVar3 * (long)(int)uVar6) {
                  uVar2 = (long)iVar3 * (long)(int)uVar6;
                }
                iVar4 = 0x65ff;
                if (uVar6 != 0) {
                  iVar4 = (DAT_ffff0000be1260dc * 500 - iVar3) + (int)(uVar2 >> 0xe);
                }
                *(uint *)(&DAT_ffff0000be126118 + uVar5 * 4) = uVar6;
              }
              (&DAT_ffff0000be1260e0)[uVar5] = iVar4;
              uVar6 = *(uint *)(lVar1 + 0xd4);
              FUN_ffff0000be05fd98
                        (s__Thermal__s_order__d_tc_num__d_t_ffff0000be0bfd5e,
                         s_lvts_tscpu_thermal_read_tc_temp_ffff0000be0bfd8e,uVar8 & 0xffffffff,
                         uVar7 & 0xffffffff,uVar5);
              iVar4 = (&DAT_ffff0000be1260e0)[uVar5];
            }
            FUN_ffff0000be05fd98
                      (s__Thermal__s_order__d_tc_num__d_t_ffff0000be0a8616,
                       s_lvts_tscpu_thermal_read_tc_temp_ffff0000be0bfd8e,uVar8 & 0xffffffff,
                       uVar7 & 0xffffffff,uVar5,iVar4,(uVar6 & 0x7fffff) * 1000 >> 10);
            uVar5 = (ulong)(uint)(&DAT_ffff0000be0de294)[uVar7 * 0x14];
          }
          uVar8 = uVar8 + 1;
        } while (uVar8 < uVar5);
      }
    }
    uVar7 = uVar7 + 1;
    puVar9 = puVar9 + 0x14;
  } while (uVar7 != 4);
  return;
}


