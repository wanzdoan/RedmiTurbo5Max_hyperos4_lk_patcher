// ffff00009078b0fc  size=408  FUN_ffff00009078b0fc

ulong FUN_ffff00009078b0fc(ulong *param_1)

{
  uint uVar1;
  ulong uVar2;
  ulong uVar3;
  uint uVar4;
  uint uVar5;
  long lVar6;
  int iVar7;
  ulong uVar8;
  ulong local_50 [4];
  
  uVar8 = param_1[2];
  if (0xffffffc0 < (int)uVar8 - 0x40U) {
    uVar2 = *param_1;
    uVar3 = param_1[1];
    local_50[2] = 0;
    local_50[3] = 0;
    local_50[0] = 0;
    local_50[1] = 0;
    iVar7 = FUN_ffff00009071c6e8();
    if (iVar7 == 2) {
      FUN_ffff00009071ebf4
                (0x82000415,0,uVar2 >> 0xc & 0xffffffff,uVar3 >> 0xc & 0xffffffff,(int)param_1[2],0,
                 0,0,local_50);
      return local_50[0] & 0xffffffff;
    }
    if (((iVar7 == 1) && ((uVar2 & 0xf80000000) != 0)) &&
       (uVar4 = (uint)(uVar3 >> 0xc) & 0xffffff, ((uint)(uVar2 >> 0xc) & 0xffffff) <= uVar4)) {
      uVar5 = (int)uVar8 - 1;
      lVar6 = (ulong)uVar5 * 8;
      uVar1 = (int)(uVar2 >> 0xc) + 0x780000U & 0x7fffff;
      uVar4 = uVar4 - 0x80000 & 0x7fffff | 0x80000000;
      *(uint *)(lVar6 + -0xffffefbd1000) = uVar1;
      *(uint *)(lVar6 + -0xffffefbd0ffc) = uVar4;
      *(uint *)(lVar6 + -0xffffefad1000) = uVar1;
      uVar8 = (ulong)(uVar5 >> 3) & 0x1ffffffc;
      *(uint *)(lVar6 + -0xffffefad0ffc) = uVar4;
      uVar4 = 1 << (ulong)(uVar5 & 0x1f);
      *(uint *)(uVar8 - 0xffffefbd0d5c) = *(uint *)(uVar8 - 0xffffefbd0d5c) | uVar4;
      *(uint *)(uVar8 - 0xffffefad0d5c) = *(uint *)(uVar8 - 0xffffefad0d5c) | uVar4;
      return 0;
    }
  }
  return 0xffffffff;
}


