// ffff0000907a8888  size=336  FUN_ffff0000907a8888

void FUN_ffff0000907a8888(undefined8 param_1,long param_2,long param_3,uint param_4)

{
  bool bVar1;
  ulong uVar2;
  uint uVar3;
  undefined1 auVar4 [16];
  undefined1 auVar5 [16];
  bool bVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong local_70 [12];
  
  uVar13 = (ulong)(param_4 * 2 - 1);
  local_70[10] = 0;
  local_70[0xb] = 0;
  local_70[8] = 0;
  local_70[9] = 0;
  local_70[6] = 0;
  local_70[7] = 0;
  local_70[4] = 0;
  local_70[5] = 0;
  local_70[2] = 0;
  local_70[3] = 0;
  local_70[0] = 0;
  local_70[1] = 0;
  uVar10 = 0;
  uVar11 = 0;
  uVar14 = 0;
  do {
    uVar12 = uVar14;
    uVar2 = uVar11 + 1;
    uVar3 = 0;
    if (param_4 <= uVar11) {
      uVar3 = (int)uVar2 - param_4;
    }
    uVar15 = (ulong)uVar3;
    uVar14 = 0;
    if (uVar3 < param_4 && uVar15 <= uVar11) {
      uVar16 = uVar11 - uVar15;
      do {
        uVar7 = *(ulong *)(param_2 + uVar15 * 8);
        uVar8 = *(ulong *)(param_3 + (uVar16 & 0xffffffff) * 8);
        uVar9 = uVar8 * uVar7;
        auVar4._8_8_ = 0;
        auVar4._0_8_ = uVar8;
        auVar5._8_8_ = 0;
        auVar5._0_8_ = uVar7;
        uVar8 = SUB168(auVar4 * auVar5,8);
        bVar6 = CARRY8(uVar9,uVar10);
        uVar10 = uVar9 + uVar10;
        uVar7 = uVar8 + uVar12;
        bVar1 = CARRY8(uVar8,uVar12);
        uVar12 = uVar8 + uVar12 + (ulong)bVar6;
        if (bVar1 || CARRY8(uVar7,(ulong)bVar6)) {
          uVar14 = uVar14 + 1;
        }
        if (uVar11 <= uVar15) break;
        uVar15 = uVar15 + 1;
        uVar16 = uVar16 - 1;
      } while (uVar15 < param_4);
    }
    local_70[uVar11] = uVar10;
    uVar10 = uVar12;
    uVar11 = uVar2;
    if (uVar2 == uVar13) {
      local_70[uVar13] = uVar12;
      switch(param_4 + 0x1ffffffe & 0x1fffffff) {
      case 0:
        FUN_ffff0000907a92c4(param_1,local_70,param_4);
        break;
      case 1:
        FUN_ffff0000907a9590(param_1,local_70,param_4);
        break;
      case 2:
        FUN_ffff0000907a97a4(param_1,local_70,param_4);
        break;
      case 4:
        FUN_ffff0000907a9bf4(param_1,local_70,param_4);
      }
      return;
    }
  } while( true );
}


