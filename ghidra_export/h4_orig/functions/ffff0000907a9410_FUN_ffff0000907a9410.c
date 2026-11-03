// ffff0000907a9410  size=372  FUN_ffff0000907a9410

void FUN_ffff0000907a9410(undefined8 param_1,long param_2,uint param_3)

{
  ulong uVar1;
  undefined1 auVar2 [16];
  undefined1 auVar3 [16];
  bool bVar4;
  bool bVar5;
  ulong uVar6;
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
  ulong uVar17;
  ulong local_80 [12];
  
  uVar14 = (ulong)(param_3 * 2 - 1);
  uVar15 = 1;
  local_80[10] = 0;
  local_80[0xb] = 0;
  local_80[8] = 0;
  local_80[9] = 0;
  local_80[6] = 0;
  local_80[7] = 0;
  local_80[4] = 0;
  local_80[5] = 0;
  local_80[2] = 0;
  local_80[3] = 0;
  local_80[0] = 0;
  local_80[1] = 0;
  uVar11 = 0;
  uVar12 = 0;
  uVar16 = 0;
  do {
    uVar13 = uVar16;
    uVar1 = uVar12 + 1;
    uVar16 = 0;
    uVar17 = 0;
    if (param_3 <= uVar12) {
      uVar17 = (ulong)((int)uVar1 - param_3);
    }
    uVar6 = uVar12 - uVar17;
    if (uVar17 <= uVar12) {
      do {
        uVar7 = uVar6 & 0xffffffff;
        if (uVar7 < uVar17) break;
        uVar8 = *(ulong *)(param_2 + uVar17 * 8);
        bVar4 = uVar17 < uVar7;
        uVar9 = *(ulong *)(param_2 + uVar7 * 8);
        uVar17 = uVar17 + 1;
        uVar6 = uVar6 - 1;
        auVar2._8_8_ = 0;
        auVar2._0_8_ = uVar9;
        auVar3._8_8_ = 0;
        auVar3._0_8_ = uVar8;
        uVar10 = SUB168(auVar2 * auVar3,8);
        uVar9 = uVar9 * uVar8;
        uVar7 = uVar10 >> 0x3f;
        uVar8 = uVar9 << bVar4;
        if (!bVar4) {
          uVar7 = 0;
        }
        bVar5 = CARRY8(uVar8,uVar11);
        uVar11 = uVar8 + uVar11;
        uVar9 = uVar10 << bVar4 | (uVar9 >> 1) >> (bVar4 ^ 0x3fU);
        uVar8 = uVar9 + uVar13;
        bVar4 = CARRY8(uVar9,uVar13);
        uVar13 = uVar9 + uVar13 + (ulong)bVar5;
        uVar16 = uVar7 + uVar16 + (ulong)(bVar4 || CARRY8(uVar8,(ulong)bVar5));
      } while (uVar15 != uVar17);
    }
    local_80[uVar12] = uVar11;
    uVar15 = uVar15 + 1;
    uVar11 = uVar13;
    uVar12 = uVar1;
    if (uVar1 == uVar14) {
      local_80[uVar14] = uVar13;
      switch(param_3 + 0x1ffffffe & 0x1fffffff) {
      case 0:
        FUN_ffff0000907a9584(param_1,local_80);
        break;
      case 1:
        FUN_ffff0000907a9850(param_1,local_80);
        break;
      case 2:
        FUN_ffff0000907a9a64(param_1,local_80);
        break;
      case 4:
        FUN_ffff0000907a9eb4(param_1,local_80);
      }
      return;
    }
  } while( true );
}


