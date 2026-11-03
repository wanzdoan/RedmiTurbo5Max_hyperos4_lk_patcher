
void FUN_ffff0000be00fbd4(uint param_1,int param_2)

{
  long lVar1;
  int *piVar2;
  undefined4 *puVar3;
  uint uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  uint uVar8;
  undefined4 uVar9;
  bool bVar10;
  long lVar11;
  long lVar12;
  int iVar13;
  int iVar14;
  ulong uVar15;
  long lVar16;
  long lVar17;
  long lVar18;
  undefined8 local_60;
  undefined4 local_58;
  
  if (0xf < param_1) {
    FUN_ffff0000be063b40(s_aee_invalid_cpu__d_ffff0000be0bab09,param_1);
    FUN_ffff0000be05fd98(s_aee_invalid_cpu__d_ffff0000be0bab09,param_1);
    return;
  }
  if (DAT_ffff0000be0fd0b0 == 0) {
    FUN_ffff0000be063b40(s_aee_invalid_ehdr_ffff0000be0a7a73);
    FUN_ffff0000be05fd98(s_aee_invalid_ehdr_ffff0000be0a7a73);
    return;
  }
  local_58 = 0;
  local_60 = 0;
  iVar13 = FUN_ffff0000be0611dc(&local_60,0xb,s_core_d_ffff0000be0bdfc4,param_1);
  iVar14 = DAT_ffff0000be0fd0c0;
  lVar12 = DAT_ffff0000be0fd0b8;
  lVar11 = DAT_ffff0000be0fb000;
  if (iVar13 < 1) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98(s_app_aee_KEDump_c_ffff0000be0a8d39,0x1e3,s_err_>_0_ffff0000be0bbb4e);
  }
  lVar1 = DAT_ffff0000be0fd0b8 + (ulong)DAT_ffff0000be0fd0c4;
  if (DAT_ffff0000be0fd0c0 == 0x28) {
    if (-1 < param_2) {
      uVar15 = (ulong)*(uint *)(lVar1 + 4);
      goto LAB_ffff0000be00fcdc;
    }
    lVar18 = 0;
    bVar10 = true;
  }
  else {
    if (param_2 < 0) {
      return;
    }
    uVar15 = *(ulong *)(lVar1 + 8);
LAB_ffff0000be00fcdc:
    piVar2 = (int *)(DAT_ffff0000be0fd0b8 + uVar15);
    piVar2 = (int *)((long)piVar2 + (ulong)(*piVar2 + piVar2[1] + 0xc));
    lVar18 = (long)piVar2 +
             (ulong)(*piVar2 + 0xcU) + (ulong)((piVar2[1] + *piVar2 + 0xcU) * param_2);
    if (DAT_ffff0000be0fd0c0 != 0x28) {
      if (lVar18 != 0) {
        lVar16 = DAT_ffff0000be0fb000 + (ulong)param_1 * 0x180;
        lVar17 = *(long *)(lVar16 + 0x2e0);
        FUN_ffff0000be0607b0(lVar18 + 0x70,lVar16 + 0x180,0x110);
        uVar9 = *(undefined4 *)(lVar11 + 8);
        *(uint *)(lVar18 + 0x20) = param_1 + 100;
        *(undefined4 *)(lVar18 + 0x24) = uVar9;
        if (lVar17 != 0) {
          *(long *)(lVar18 + 0x10) = lVar17;
        }
      }
      uVar15 = *(ulong *)(lVar1 + 8);
      goto LAB_ffff0000be00fdd0;
    }
    bVar10 = false;
  }
  lVar16 = DAT_ffff0000be0fb000 + (ulong)param_1 * 0x180;
  uVar8 = *(uint *)(lVar16 + 0x1b4);
  uVar4 = 0;
  if (0x2000 < uVar8) {
    uVar4 = uVar8 & 0xffffe000 | 0xc;
  }
  if (lVar18 != 0) {
    *(undefined8 *)(lVar18 + 0x50) = *(undefined8 *)(lVar16 + 0x180);
    uVar6 = *(undefined8 *)(lVar16 + 0x1a0);
    uVar5 = *(undefined8 *)(lVar16 + 0x188);
    uVar7 = *(undefined8 *)(lVar16 + 400);
    *(undefined8 *)(lVar18 + 0x68) = *(undefined8 *)(lVar16 + 0x198);
    *(undefined8 *)(lVar18 + 0x70) = uVar6;
    *(undefined8 *)(lVar18 + 0x58) = uVar5;
    *(undefined8 *)(lVar18 + 0x60) = uVar7;
    uVar6 = *(undefined8 *)(lVar16 + 0x1c0);
    uVar5 = *(undefined8 *)(lVar16 + 0x1a8);
    uVar7 = *(undefined8 *)(lVar16 + 0x1b0);
    *(undefined8 *)(lVar18 + 0x88) = *(undefined8 *)(lVar16 + 0x1b8);
    *(undefined8 *)(lVar18 + 0x90) = uVar6;
    *(undefined8 *)(lVar18 + 0x78) = uVar5;
    *(undefined8 *)(lVar18 + 0x80) = uVar7;
    uVar9 = *(undefined4 *)(lVar11 + 8);
    *(uint *)(lVar18 + 0x20) = param_1 + 100;
    *(undefined4 *)(lVar18 + 0x24) = uVar9;
    if (0x2000 < uVar8) {
      *(ulong *)(lVar18 + 0x10) = (ulong)uVar4;
    }
  }
  if (bVar10) {
    return;
  }
  uVar15 = (ulong)*(uint *)(lVar1 + 4);
LAB_ffff0000be00fdd0:
  piVar2 = (int *)(lVar12 + uVar15);
  piVar2 = (int *)((long)piVar2 + (ulong)(*piVar2 + piVar2[1] + 0xc));
  puVar3 = (undefined4 *)((long)piVar2 + (ulong)(uint)((*piVar2 + piVar2[1] + 0xc) * param_2));
  if (iVar14 == 0x28) {
    uVar15 = (ulong)*(uint *)(lVar1 + 4);
  }
  else {
    uVar15 = *(ulong *)(lVar1 + 8);
  }
  if (puVar3 != (undefined4 *)0x0) {
    piVar2 = (int *)(lVar12 + uVar15);
    iVar14 = *piVar2;
    iVar13 = piVar2[1];
    *puVar3 = 0xc;
    puVar3[1] = *(undefined4 *)((long)piVar2 + (ulong)(iVar14 + iVar13 + 0xc) + 4);
    puVar3[2] = 1;
    iVar14 = FUN_ffff0000be0611dc(puVar3 + 3,0xc,&DAT_ffff0000be0ac42b,&local_60);
    if (iVar14 < 0) {
      FUN_ffff0000be063b40(s__s__snprintf_fail_ffff0000be0bcd41,s_fill_note_ffff0000be0b0920);
      FUN_ffff0000be05fd98(s__s__snprintf_fail_ffff0000be0bcd41,s_fill_note_ffff0000be0b0920);
    }
  }
  return;
}


