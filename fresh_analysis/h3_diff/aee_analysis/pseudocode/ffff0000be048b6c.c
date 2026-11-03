
undefined8 FUN_ffff0000be048b6c(uint *param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  undefined8 uVar5;
  ulong uVar6;
  int iVar7;
  long lVar8;
  int *piVar9;
  undefined8 uVar10;
  undefined4 local_9d8 [6];
  uint auStack_9c0 [3];
  undefined8 uStack_9b4;
  uint auStack_9ac [2];
  undefined8 auStack_9a4 [2];
  uint auStack_994 [4];
  undefined8 uStack_984;
  uint uStack_97c;
  undefined1 auStack_978 [8];
  undefined8 uStack_970;
  uint auStack_968 [156];
  undefined1 auStack_6f8 [1604];
  undefined4 local_b4;
  undefined4 local_b0;
  undefined4 local_ac;
  
  FUN_ffff0000be060ae0(local_9d8,0,0x998);
  FUN_ffff0000be060ae0(local_9d8,0,0x998);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  lVar8 = FUN_ffff0000be041538(DAT_ffff0000be1260c8);
  if (lVar8 != 0) {
    FUN_ffff0000be0607b0(local_9d8,lVar8,0x998);
  }
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260a8 != 0) {
    return 0;
  }
  iVar7 = FUN_ffff0000be041774(DAT_ffff0000be1260c8);
  if (iVar7 != 0) {
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    iVar7 = FUN_ffff0000be047450(DAT_ffff0000be1260b0);
    if (iVar7 != 0) {
      if ((DAT_ffff0000be124adc & 1) == 0) {
        FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
        DAT_ffff0000be124adc = 1;
      }
      FUN_ffff0000be041af4(DAT_ffff0000be1260c8,1,100);
    }
  }
  uVar1 = param_1[1];
  uVar10 = *(undefined8 *)(param_1 + 2);
  uVar6 = (ulong)*param_1;
  auStack_9c0[uVar6 * 0x1a] = *param_1;
  auStack_9c0[uVar6 * 0x1a + 1] = uVar1;
  uVar1 = param_1[6];
  uVar2 = param_1[7];
  (&uStack_9b4)[uVar6 * 0xd] = uVar10;
  uVar10 = *(undefined8 *)(param_1 + 8);
  uVar5 = *(undefined8 *)(param_1 + 10);
  auStack_9ac[uVar6 * 0x1a] = uVar1;
  auStack_9ac[uVar6 * 0x1a + 1] = uVar2;
  auStack_9a4[uVar6 * 0xd] = uVar10;
  uVar1 = param_1[0xc];
  uVar3 = param_1[0xd];
  auStack_9a4[uVar6 * 0xd + 1] = uVar5;
  uVar2 = param_1[0xe];
  uVar4 = param_1[0xf];
  auStack_994[uVar6 * 0x1a] = uVar1;
  auStack_994[uVar6 * 0x1a + 1] = uVar3;
  uVar10 = *(undefined8 *)(param_1 + 0x10);
  uVar1 = param_1[0x12];
  auStack_994[uVar6 * 0x1a + 2] = uVar2;
  auStack_994[uVar6 * 0x1a + 3] = uVar4;
  uVar2 = param_1[0x13];
  (&uStack_984)[uVar6 * 0xd] = uVar10;
  uVar10 = *(undefined8 *)(param_1 + 0x15);
  (&uStack_97c)[uVar6 * 0x1a] = uVar1;
  auStack_978[uVar6 * 0x68] = (char)uVar2;
  uVar3 = param_1[0x19];
  (&uStack_970)[uVar6 * 0xd] = uVar10;
  uVar1 = param_1[0x17];
  uVar2 = param_1[0x18];
  auStack_968[uVar6 * 0x1a + 2] = uVar3;
  auStack_968[uVar6 * 0x1a] = uVar1;
  auStack_968[uVar6 * 0x1a + 1] = uVar2;
  local_9d8[0] = 1;
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  piVar9 = (int *)FUN_ffff0000be047288(DAT_ffff0000be1260b0);
  FUN_ffff0000be0607b0(auStack_6f8,piVar9 + 0x5c,0x5b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  local_b0 = FUN_ffff0000be0472e8(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  local_ac = FUN_ffff0000be047328(DAT_ffff0000be1260b0);
  if (*piVar9 == 1) {
    uVar1 = piVar9[0x4e];
  }
  else {
    if (*piVar9 != 2) goto LAB_ffff0000be048df4;
    uVar1 = piVar9[0x65];
  }
  if (uVar1 < 3) {
    local_b4 = *(undefined4 *)(&DAT_ffff0000be0cc3d0 + (long)(int)uVar1 * 4);
  }
LAB_ffff0000be048df4:
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar10 = FUN_ffff0000be04125c(DAT_ffff0000be1260c8,local_9d8,DAT_ffff0000be126088);
  return uVar10;
}


