
void FUN_ffff0000be044704(long param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  undefined *puVar4;
  ulong uVar5;
  uint uVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  long lVar13;
  ulong uVar14;
  
  puVar4 = PTR_DAT_ffff0000be0f5658;
  uVar10 = 0;
  do {
    if (*(int *)(param_1 + uVar10 * 4) == -1) {
      if ((uint)uVar10 < 2) {
        return;
      }
      break;
    }
    uVar10 = uVar10 + 1;
  } while (uVar10 != 0x4b);
  uVar9 = 0;
  uVar12 = 0;
  uVar11 = 1;
  do {
    iVar1 = *(int *)(param_1 + uVar9 * 4);
    uVar14 = 0;
    uVar6 = 0;
    do {
      uVar5 = uVar11;
      if (iVar1 == (&DAT_ffff0000be0dd490)[uVar14 * 0x1a]) {
        do {
          uVar8 = uVar5;
          iVar2 = *(int *)(param_1 + uVar8 * 4);
          uVar5 = uVar8 + 1;
        } while (*(int *)(puVar4 + (long)iVar2 * 0x20 + 0x10) == 0 && uVar8 < (uVar10 & 0xffffffff))
        ;
        if ((uVar10 & 0xffffffff) <= uVar8) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000be0acca6,0x619,
                     s_step_<_module_num_ffff0000be0a7360);
        }
        if ((&DAT_ffff0000be0dd494)[uVar14 * 0x1a] == -1) {
LAB_ffff0000be044784:
          uVar7 = 0;
        }
        else {
          if ((&DAT_ffff0000be0dd494)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 0;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd49c)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd49c)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 1;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd4a4)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd4a4)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 2;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd4ac)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd4ac)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 3;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd4b4)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd4b4)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 4;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd4bc)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd4bc)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 5;
            goto LAB_ffff0000be0448e0;
          }
          if ((&DAT_ffff0000be0dd4c4)[uVar14 * 0x1a] == -1) goto LAB_ffff0000be044784;
          if ((&DAT_ffff0000be0dd4c4)[uVar14 * 0x1a] == iVar2) {
            lVar13 = 6;
          }
          else {
            uVar7 = 0;
            if (((&DAT_ffff0000be0dd4cc)[uVar14 * 0x1a] == -1) ||
               ((&DAT_ffff0000be0dd4cc)[uVar14 * 0x1a] != iVar2)) goto LAB_ffff0000be044788;
            lVar13 = 7;
          }
LAB_ffff0000be0448e0:
          uVar7 = (&DAT_ffff0000be0dd498)[lVar13 * 2 + uVar14 * 0x1a];
          uVar12 = uVar14 & 0xffffffff;
          uVar6 = uVar7 | uVar6;
        }
LAB_ffff0000be044788:
        (&DAT_ffff0000be0dd4e0)[uVar14 * 0x1a] =
             (&DAT_ffff0000be0dd4e0)[uVar14 * 0x1a] & (uVar7 ^ 0xffffffff);
      }
      uVar14 = uVar14 + 1;
    } while (uVar14 != 0x1b);
    if (uVar6 != 0) {
      lVar13 = uVar12 * 0x1a;
      lVar3 = uVar12 * 0xd;
      uVar12 = 0;
      *(undefined4 *)(&DAT_ffff0000be0dd4d8)[lVar3] = (&DAT_ffff0000be0dd4e0)[lVar13];
    }
    uVar9 = uVar9 + 1;
    uVar11 = uVar11 + 1;
    if (uVar9 == (int)uVar10 - 1) {
      return;
    }
  } while( true );
}


