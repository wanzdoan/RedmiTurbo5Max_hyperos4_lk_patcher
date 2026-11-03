
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b803b090(long param_1)

{
  int iVar1;
  int iVar2;
  undefined *puVar3;
  ulong uVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  uint uVar13;
  
  puVar3 = PTR_DAT_ffff0000b8108758;
  uVar8 = 0;
  do {
    if (*(int *)(param_1 + uVar8 * 4) == -1) {
      if ((uint)uVar8 < 2) goto LAB_ffff0000b803b28c;
      break;
    }
    uVar8 = uVar8 + 1;
  } while (uVar8 != 0x4b);
  uVar7 = 0;
  uVar10 = 0;
  uVar9 = 1;
  do {
    iVar1 = *(int *)(param_1 + uVar7 * 4);
    uVar12 = 0;
    uVar13 = 0;
    do {
      uVar4 = uVar9;
      if (iVar1 == (&DAT_ffff0000b80f03c0)[uVar12 * 0x1a]) {
        do {
          uVar6 = uVar4;
          iVar2 = *(int *)(param_1 + uVar6 * 4);
          uVar4 = uVar6 + 1;
        } while (*(int *)(puVar3 + (long)iVar2 * 0x20 + 0x10) == 0 && uVar6 < (uVar8 & 0xffffffff));
        if ((uVar8 & 0xffffffff) <= uVar6) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b830
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000b808caf3,0x597,
                     s_step_<_module_num_ffff0000b8088690);
        }
        uVar5 = (&DAT_ffff0000b80f0410)[uVar12 * 0x1a];
        if ((&DAT_ffff0000b80f03c4)[uVar12 * 0x1a] != -1) {
          if ((&DAT_ffff0000b80f03c4)[uVar12 * 0x1a] == iVar2) {
            lVar11 = 0;
          }
          else {
            if ((&DAT_ffff0000b80f03cc)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
            if ((&DAT_ffff0000b80f03cc)[uVar12 * 0x1a] == iVar2) {
              lVar11 = 1;
            }
            else {
              if ((&DAT_ffff0000b80f03d4)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
              if ((&DAT_ffff0000b80f03d4)[uVar12 * 0x1a] == iVar2) {
                lVar11 = 2;
              }
              else {
                if ((&DAT_ffff0000b80f03dc)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
                if ((&DAT_ffff0000b80f03dc)[uVar12 * 0x1a] == iVar2) {
                  lVar11 = 3;
                }
                else {
                  if ((&DAT_ffff0000b80f03e4)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
                  if ((&DAT_ffff0000b80f03e4)[uVar12 * 0x1a] == iVar2) {
                    lVar11 = 4;
                  }
                  else {
                    if ((&DAT_ffff0000b80f03ec)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
                    if ((&DAT_ffff0000b80f03ec)[uVar12 * 0x1a] == iVar2) {
                      lVar11 = 5;
                    }
                    else {
                      if ((&DAT_ffff0000b80f03f4)[uVar12 * 0x1a] == -1) goto LAB_ffff0000b803b12c;
                      if ((&DAT_ffff0000b80f03f4)[uVar12 * 0x1a] == iVar2) {
                        lVar11 = 6;
                      }
                      else {
                        if (((&DAT_ffff0000b80f03fc)[uVar12 * 0x1a] == -1) ||
                           ((&DAT_ffff0000b80f03fc)[uVar12 * 0x1a] != iVar2))
                        goto LAB_ffff0000b803b12c;
                        lVar11 = 7;
                      }
                    }
                  }
                }
              }
            }
          }
          uVar5 = (&DAT_ffff0000b80f03c8)[lVar11 * 2 + uVar12 * 0x1a] | uVar5;
          uVar10 = uVar12 & 0xffffffff;
          uVar13 = uVar5 | uVar13;
        }
LAB_ffff0000b803b12c:
        (&DAT_ffff0000b80f0410)[uVar12 * 0x1a] = uVar5;
      }
      uVar12 = uVar12 + 1;
    } while (uVar12 != 0x1b);
    if (uVar13 != 0) {
      lVar11 = uVar10 * 0xd;
      uVar10 = 0;
      *(uint *)(&DAT_ffff0000b80f0408)[lVar11] = uVar13;
    }
    uVar7 = uVar7 + 1;
    uVar9 = uVar9 + 1;
  } while (uVar7 != (int)uVar8 - 1);
LAB_ffff0000b803b28c:
  _DAT_ffff000032000dd0 = _DAT_ffff000032000dd0 | 4;
  _DAT_ffff000032000e3c = _DAT_ffff000032000e3c | 0x400;
  _DAT_ffff000032000e54 = _DAT_ffff000032000e54 | 0x800;
  return;
}


