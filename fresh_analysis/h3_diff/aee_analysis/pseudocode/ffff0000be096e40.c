
undefined8 FUN_ffff0000be096e40(long *param_1,long param_2,undefined4 param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  ulong uVar7;
  int iVar8;
  ulong uVar9;
  uint uVar10;
  long lVar11;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  
  if (((*(int *)((long)param_1 + 0xc) != 0) && (param_1[2] != 0)) &&
     (iVar4 = FUN_ffff0000be056cbc(param_2,param_3), -1 < iVar4)) {
    iVar4 = FUN_ffff0000be055e80(*(undefined8 *)*param_1);
    if (0 < iVar4) {
      iVar8 = 0;
      do {
        local_70 = 0;
        local_58 = 0;
        FUN_ffff0000be055dc4(*(undefined8 *)*param_1,iVar8,&local_70,&local_58);
        FUN_ffff0000be056d2c(param_2,local_70,local_58);
        iVar8 = iVar8 + 1;
      } while (iVar4 != iVar8);
    }
    iVar4 = FUN_ffff0000be056de0(param_2);
    if (-1 < iVar4) {
      if (*(int *)((long)param_1 + 0xc) < 1) {
        uVar10 = 0;
      }
      else {
        lVar11 = 0;
        uVar10 = 0;
        uVar1 = (*(uint *)(param_2 + 8) & 0xff00ff00) >> 8 |
                (*(uint *)(param_2 + 8) & 0xff00ff) << 8;
        uVar2 = (*(uint *)(param_2 + 0x24) & 0xff00ff00) >> 8 |
                (*(uint *)(param_2 + 0x24) & 0xff00ff) << 8;
        uVar3 = (*(uint *)(param_2 + 4) & 0xff00ff00) >> 8 |
                (*(uint *)(param_2 + 4) & 0xff00ff) << 8;
        uVar7 = param_2 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10);
        do {
          lVar6 = *(long *)(*param_1 + lVar11 * 8);
          uVar9 = (long)(int)((uint)*(byte *)(lVar6 + 0x20) << 0x18) |
                  (ulong)*(byte *)(lVar6 + 0x21) << 0x10 | (ulong)*(byte *)(lVar6 + 0x22) << 8 |
                  (ulong)*(byte *)(lVar6 + 0x23);
          uVar7 = uVar7 - uVar9;
          if (uVar7 < param_2 + (ulong)(uVar1 >> 0x10 | uVar1 << 0x10) +
                      (ulong)(uVar2 >> 0x10 | uVar2 << 0x10)) {
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,s__ufdt_output_strtab_to_fdt_ffff0000be0c4b0b
                      );
            FUN_ffff0000be09664c(s_Not_enough_space_for_string_tabl_ffff0000be0b5ed7);
            return 0xffffffff;
          }
          uVar3 = (*(uint *)(lVar6 + 0xc) & 0xff00ff00) >> 8 |
                  (*(uint *)(lVar6 + 0xc) & 0xff00ff) << 8;
          thunk_FUN_ffff0000be0607b0(uVar7,lVar6 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10),uVar9);
          uVar10 = (int)uVar9 + uVar10;
          lVar11 = lVar11 + 1;
        } while (lVar11 < *(int *)((long)param_1 + 0xc));
      }
      uVar10 = (uVar10 & 0xff00ff00) >> 8 | (uVar10 & 0xff00ff) << 8;
      local_68 = 0;
      uStack_60 = 0;
      local_70 = 0;
      *(uint *)(param_2 + 0x20) = uVar10 >> 0x10 | uVar10 << 0x10;
      iVar4 = FUN_ffff0000be098d34(&local_70,param_2);
      if (-1 < iVar4) {
        iVar4 = FUN_ffff0000be097038(param_1,param_2,param_1[2],&local_70);
        FUN_ffff0000be098d98(&local_70);
        if ((-1 < iVar4) && (iVar4 = FUN_ffff0000be057004(param_2), -1 < iVar4)) {
          uVar5 = FUN_ffff0000be0568a8(param_2,param_2,param_3);
          return uVar5;
        }
      }
    }
  }
  return 0xffffffff;
}


