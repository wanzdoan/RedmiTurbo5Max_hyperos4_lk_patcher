
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b8037e98(undefined8 *param_1,long param_2,int param_3)

{
  undefined *puVar1;
  uint uVar2;
  undefined4 uVar3;
  uint uVar4;
  long lVar5;
  uint *puVar6;
  uint uVar7;
  long lVar8;
  code *pcVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  ulong uVar12;
  ulong uVar13;
  uint uVar14;
  undefined4 local_80;
  uint local_7c;
  undefined4 local_68 [2];
  
  if (param_1 == (undefined8 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x40c,
               s_dp_handle____NULL_ffff0000b8092b68);
  }
  FUN_ffff0000b803b000(*(undefined4 *)(param_1 + 0x1c));
  FUN_ffff0000b803ad00(*(undefined4 *)(param_1 + 0x1c));
  if (param_3 == 0) {
    uVar11 = 0;
  }
  else {
    uVar11 = *param_1;
  }
  uVar3 = *(undefined4 *)(param_1 + 0x1c);
  local_68[1] = 0x14;
  local_68[0] = uVar3;
  uVar2 = FUN_ffff0000b803acd4(uVar3);
  if ((uVar2 & 1) == 0) {
    lVar5 = 0;
  }
  else {
    uVar3 = FUN_ffff0000b803ace8(uVar3);
    local_68[1] = uVar3;
    lVar5 = FUN_ffff0000b804bccc(0x998);
    if (lVar5 == 0) {
      return 0xffffffff;
    }
    *(undefined1 *)(param_2 + 0x990) = 1;
  }
  FUN_ffff0000b804cd40(param_1 + 0x1d,param_2,0x998);
  puVar1 = PTR_DAT_ffff0000b8108760;
  local_80 = 0;
  local_7c = 0;
  lVar8 = 0;
  uVar14 = 1;
  do {
    uVar3 = local_68[lVar8];
    puVar6 = (uint *)FUN_ffff0000b803b000(uVar3);
    uVar4 = FUN_ffff0000b803ad00(uVar3);
    uVar13 = (ulong)uVar4;
    if ((uVar2 & 1) != 0) {
      *(undefined1 *)(param_2 + 0x2d8) = 1;
      uVar7 = *(uint *)(param_2 + 0x928) >> 1;
      *(uint *)(param_2 + 0x2c8) = uVar7;
      if (uVar14 == 0) {
        *(uint *)(param_2 + 0x2d0) = *(uint *)(param_2 + 0x928);
        *(uint *)(param_2 + 0x2c8) = local_7c;
        *(undefined4 *)(param_2 + 0x2cc) = local_80;
        uVar7 = local_7c;
      }
      local_7c = uVar7;
      if (0 < (int)uVar4) {
        uVar12 = uVar13 + 1;
        do {
          if ((*(long *)(puVar1 + (ulong)puVar6[(int)uVar12 - 2] * 8) != 0) &&
             (pcVar9 = *(code **)(*(long *)(puVar1 + (ulong)puVar6[(int)uVar12 - 2] * 8) + 0xb0),
             pcVar9 != (code *)0x0)) {
            (*pcVar9)((ulong)puVar6[(int)uVar12 - 2],param_2);
          }
          uVar12 = uVar12 - 1;
        } while (1 < uVar12);
        local_7c = *(uint *)(param_2 + 0x2c8);
      }
      uVar10 = *(undefined8 *)(param_2 + 0x2d0);
      uVar3 = *(undefined4 *)(param_2 + 0x2d8);
      local_80 = *(undefined4 *)(param_2 + 0x2cc);
      if (uVar14 == 0) {
        DAT_ffff0000b8128f30 = *(undefined8 *)(param_2 + 0x2c8);
        DAT_ffff0000b8128f38 = *(undefined8 *)(param_2 + 0x2d0);
        _DAT_ffff0000b8128f40 = *(undefined4 *)(param_2 + 0x2d8);
      }
      FUN_ffff0000b8037c08(param_1 + 0x1d,lVar5,uVar14);
      *(undefined4 *)(lVar5 + 0x2cc) = local_80;
      *(uint *)(lVar5 + 0x2c8) = local_7c;
      *(undefined8 *)(lVar5 + 0x2d0) = uVar10;
      *(undefined4 *)(lVar5 + 0x2d8) = uVar3;
      *(uint *)(lVar5 + 0x928) = *(uint *)(lVar5 + 0x928) >> 1;
      param_2 = lVar5;
    }
    if (0 < (int)uVar4) {
      do {
        uVar4 = *puVar6;
        uVar12 = (ulong)uVar4;
        if (*(long *)(puVar1 + uVar12 * 8) != 0) {
          pcVar9 = *(code **)(*(long *)(puVar1 + uVar12 * 8) + 0x20);
          if (pcVar9 != (code *)0x0) {
            (*pcVar9)(uVar12,param_2,uVar11);
          }
          if (((uVar4 < 0x1f) && ((1 << (ulong)(uVar4 & 0x1f) & 0x5d7c6000U) != 0)) &&
             (*(code **)(*(long *)(puVar1 + uVar12 * 8) + 0x80) != (code *)0x0)) {
            (**(code **)(*(long *)(puVar1 + uVar12 * 8) + 0x80))(uVar4,1);
          }
        }
        uVar13 = uVar13 - 1;
        puVar6 = puVar6 + 1;
      } while (uVar13 != 0);
    }
    uVar4 = uVar14 & uVar2;
    lVar8 = 1;
    uVar14 = 0;
  } while (uVar4 != 0);
  if ((uVar2 & 1) != 0) {
    thunk_FUN_ffff0000b8082558(lVar5);
  }
  return 0;
}


