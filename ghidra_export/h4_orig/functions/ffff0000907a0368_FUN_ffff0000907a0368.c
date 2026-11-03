// ffff0000907a0368  size=344  FUN_ffff0000907a0368

int FUN_ffff0000907a0368(long param_1,undefined8 param_2,undefined1 *param_3,long param_4)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  uint *puVar5;
  undefined1 *puVar6;
  uint uVar7;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  uint local_70 [8];
  
  local_70[4] = 0;
  local_70[5] = 0;
  local_70[6] = 0;
  local_70[7] = 0;
  iVar2 = 0x102012;
  local_70[0] = 0;
  local_70[1] = 0;
  local_70[2] = 0;
  local_70[3] = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  local_140 = 0;
  uStack_138 = 0;
  local_150 = 0;
  uStack_148 = 0;
  if ((param_1 != 0) && (param_3 != (undefined1 *)0x0)) {
    if (param_4 != 0) {
      uVar7 = 0;
      do {
        uVar1 = (uVar7 & 0xff00ff00) >> 8 | (uVar7 & 0xff00ff) << 8;
        local_70[0] = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff0000907a4398(&local_150);
        if ((((iVar2 != 0) || (iVar2 = FUN_ffff0000907a4410(&local_150,param_1,param_2), iVar2 != 0)
             ) || (iVar2 = FUN_ffff0000907a4410(&local_150,local_70,4), iVar2 != 0)) ||
           (iVar2 = FUN_ffff0000907a451c(&local_150,local_70), iVar2 != 0))
        goto LAB_ffff0000907a047c;
        uVar3 = param_4 - 1;
        uVar7 = uVar7 + 1;
        if (0x1e < uVar3) {
          uVar3 = 0x1f;
        }
        lVar4 = uVar3 + 1;
        puVar5 = local_70;
        puVar6 = param_3;
        do {
          param_4 = param_4 + -1;
          lVar4 = lVar4 + -1;
          param_3 = puVar6 + 1;
          *puVar6 = (char)*puVar5;
          puVar5 = (uint *)((long)puVar5 + 1);
          puVar6 = param_3;
        } while (lVar4 != 0);
      } while (param_4 != 0);
    }
    iVar2 = 0;
LAB_ffff0000907a047c:
    FUN_ffff00009077ee50(local_70,0,0x20);
    FUN_ffff00009077ee50(&local_150,0,0xe0);
  }
  return iVar2;
}


