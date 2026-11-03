// ffff00009078f4c4  size=532  FUN_ffff00009078f4c4

undefined4 FUN_ffff00009078f4c4(undefined8 param_1,undefined4 param_2,ulong param_3,ulong param_4)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 *puVar3;
  ulong uVar4;
  long lVar5;
  uint uVar6;
  code *pcVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  uint uVar11;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined4 *local_68;
  undefined4 local_44;
  
  uVar9 = param_4 & 0xffffffff;
  uVar8 = param_3 & 0xffffffff;
  puVar3 = (undefined4 *)FUN_ffff00009077d294(0x40,0x100);
  if (puVar3 == (undefined4 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_ufs_ufs_ffff0000907fe1de,0x177,
               s_ufs_temp_buf_ffff0000907d03b0);
  }
  local_70 = 0;
  local_68 = (undefined4 *)0x0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  local_88 = 0;
  local_44 = 0;
  uVar4 = FUN_ffff00009078fca8(param_1,&local_44);
  uVar1 = local_44;
  if ((uVar4 & 1) == 0) {
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077eb88((long)&local_88 + 5,0,0x10);
    *(char *)(puVar3 + 3) = (char)(uVar8 >> 0x18);
    *puVar3 = 0x10001600;
    *(char *)((long)puVar3 + 0xd) = (char)(uVar8 >> 0x10);
    *(char *)(puVar3 + 4) = (char)(uVar9 >> 0x18);
    *(char *)((long)puVar3 + 0xe) = (char)(uVar8 >> 8);
    *(char *)((long)puVar3 + 0x11) = (char)(uVar9 >> 0x10);
    local_70 = CONCAT44(local_70._4_4_,0x18);
    *(char *)((long)puVar3 + 0x12) = (char)(uVar9 >> 8);
    puVar3[2] = 0;
    local_90 = CONCAT44(uVar1,param_2);
    *(char *)((long)puVar3 + 0xf) = (char)param_3;
    uVar6 = (uint)param_4;
    *(char *)((long)puVar3 + 0x13) = (char)param_4;
    local_88 = CONCAT26(local_88._6_2_,0x420000000001);
    uStack_78 = CONCAT26(10,(undefined6)uStack_78);
    local_80._0_6_ = CONCAT24(0x1800,(undefined4)local_80);
    local_68 = puVar3;
    uVar1 = FUN_ffff000090790d5c(param_1,&local_90);
    FUN_ffff00009078fd78(param_1,local_44);
    if (0x3fff < uVar6) {
      uVar6 = 0x4000;
    }
    if (uVar6 != 0) {
      uVar11 = uVar6 & 0x7f;
      do {
        lVar5 = FUN_ffff000090792e88(0);
        pcVar7 = *(code **)(lVar5 + 0x200);
        if (uVar6 < 0x80) {
          if (pcVar7 != (code *)0x0) {
            lVar10 = -0xffff6f775e40;
            goto LAB_ffff00009078f680;
          }
          break;
        }
        if (pcVar7 != (code *)0x0) {
          (*pcVar7)(lVar5,param_2,uVar8,0x80,0xffff00009088a1c0);
        }
        uVar8 = (ulong)((int)uVar8 + 0x80);
        uVar6 = uVar6 - 0x80;
      } while (uVar6 != 0);
    }
  }
  goto LAB_ffff00009078f640;
  while( true ) {
    uVar8 = (ulong)(uVar6 + (int)uVar8);
    uVar11 = uVar11 - uVar6;
    lVar10 = lVar10 + (ulong)(uVar6 << 9) * 8;
    if (uVar11 == 0) break;
LAB_ffff00009078f680:
    uVar6 = uVar11;
    if (0x1fff < uVar11) {
      uVar6 = 0x2000;
    }
    iVar2 = (**(code **)(lVar5 + 0x200))(lVar5,param_2,uVar8,uVar6,lVar10);
    if (iVar2 != 0) break;
  }
LAB_ffff00009078f640:
  thunk_FUN_ffff0000907c2ad8(puVar3);
  return uVar1;
}


