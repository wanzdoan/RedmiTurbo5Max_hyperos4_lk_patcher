// ffff0000907565bc  size=652  FUN_ffff0000907565bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907565bc(long param_1,long param_2,int param_3)

{
  undefined4 uVar1;
  long lVar2;
  uint uVar3;
  undefined4 uVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  ulong uVar8;
  uint uVar9;
  long lVar10;
  code *pcVar11;
  undefined8 uVar12;
  uint uVar13;
  ulong uVar14;
  long lVar15;
  uint uVar16;
  undefined8 local_68;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x531,
               s_dp_handle____NULL_ffff0000907e81a2);
  }
  FUN_ffff0000907593e8(*(undefined4 *)(param_1 + 0xe0));
  FUN_ffff0000907590e8(*(undefined4 *)(param_1 + 0xe0));
  local_68 = 0x1400000014;
  thunk_FUN_ffff00009075d768();
  uVar4 = *(undefined4 *)(param_1 + 0xe0);
  local_68 = CONCAT44(local_68._4_4_,uVar4);
  uVar3 = FUN_ffff0000907590bc(uVar4);
  if ((uVar3 & 1) == 0) {
    lVar6 = 0;
  }
  else {
    uVar4 = FUN_ffff0000907590d0(uVar4);
    local_68 = CONCAT44(uVar4,(undefined4)local_68);
    lVar6 = FUN_ffff00009077d514(0x998);
    if (lVar6 == 0) {
      return 0xffffffff;
    }
    *(undefined1 *)(param_2 + 0x990) = 1;
  }
  FUN_ffff00009077eb20(param_1 + 0xe8,param_2,0x998);
  lVar2 = DAT_ffff0000908399c8;
  lVar15 = 0;
  uVar4 = 0;
  uVar13 = 0;
  uVar16 = 1;
  do {
    uVar1 = *(undefined4 *)((long)&local_68 + lVar15 * 4);
    lVar7 = FUN_ffff0000907593e8(uVar1);
    uVar5 = FUN_ffff0000907590e8(uVar1);
    if ((uVar3 & 1) != 0) {
      uVar9 = *(uint *)(param_2 + 0x928) >> 1;
      *(undefined1 *)(param_2 + 0x2d8) = 1;
      *(uint *)(param_2 + 0x2c8) = uVar9;
      if (uVar16 == 0) {
        *(uint *)(param_2 + 0x2d0) = *(uint *)(param_2 + 0x928);
        *(uint *)(param_2 + 0x2c8) = uVar13;
        *(undefined4 *)(param_2 + 0x2cc) = uVar4;
        uVar9 = uVar13;
      }
      uVar13 = uVar9;
      if (0 < (int)uVar5) {
        uVar14 = (ulong)uVar5 + 1;
        do {
          uVar8 = (ulong)*(uint *)(lVar7 + (ulong)((int)uVar14 - 2) * 4);
          lVar10 = *(long *)(lVar2 + uVar8 * 8);
          if ((lVar10 != 0) && (pcVar11 = *(code **)(lVar10 + 0xb0), pcVar11 != (code *)0x0)) {
            (*pcVar11)(uVar8,param_2);
          }
          uVar14 = uVar14 - 1;
        } while (1 < uVar14);
        uVar13 = *(uint *)(param_2 + 0x2c8);
      }
      uVar12 = *(undefined8 *)(param_2 + 0x2d0);
      uVar1 = *(undefined4 *)(param_2 + 0x2d8);
      uVar4 = *(undefined4 *)(param_2 + 0x2cc);
      if (uVar16 == 0) {
        _DAT_ffff00009087b5f0 = *(undefined8 *)(param_2 + 0x2c8);
        _DAT_ffff00009087b5f8 = *(undefined8 *)(param_2 + 0x2d0);
        _DAT_ffff00009087b600 = *(undefined4 *)(param_2 + 0x2d8);
      }
      FUN_ffff00009077eb20(param_1 + 0xfc,param_2 + 0x14,0x68);
      FUN_ffff00009077eb20(lVar6 + 0x14,param_2 + 0x14,0x68);
      FUN_ffff000090755d98(param_1 + 0xe8,lVar6,uVar16);
      *(uint *)(lVar6 + 0x2c8) = uVar13;
      *(undefined4 *)(lVar6 + 0x2cc) = uVar4;
      *(undefined8 *)(lVar6 + 0x2d0) = uVar12;
      *(undefined4 *)(lVar6 + 0x2d8) = uVar1;
      *(uint *)(lVar6 + 0x928) = *(uint *)(lVar6 + 0x928) >> 1;
      param_2 = lVar6;
    }
    uVar5 = uVar16 & uVar3;
    uVar16 = 0;
    uVar14 = lVar15 << 0x16;
    lVar15 = 1;
    *(int *)(uVar14 | 0xffff000032860f40) = param_3 + *(int *)(param_2 + 0x30) * 4;
  } while (uVar5 != 0);
  if ((uVar3 & 1) != 0) {
    thunk_FUN_ffff0000907c314c(lVar6);
  }
  return 0;
}


