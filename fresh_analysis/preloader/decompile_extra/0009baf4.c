// File offset entry: 0x9baf4

ulong FUN_0209b9f4(uint *param_1,long param_2)

{
  uint uVar1;
  ulong uVar2;
  long lVar3;
  uint *puVar4;
  undefined1 auVar5 [12];
  uint uStack_3c;
  long lStack_38;
  
  if ((param_1 == (uint *)0x0) || (param_2 == 0)) {
    return 2;
  }
  uVar2 = 0x7247;
  lStack_38 = *plRam0000000002103280;
  uStack_3c = 0;
  puVar4 = param_1;
  if ((param_1 != (uint *)0x0) && (param_2 != 0)) {
    uVar1 = func_0x020abff8(0x18000000,&uStack_3c);
    if (uVar1 == 0) {
      uVar2 = 0x7255;
      if ((uStack_3c < 8) && ((1 << (ulong)(uStack_3c & 0x1f) & 0xa3U) != 0)) {
        uVar2 = func_0x020ab95c(0x18003000,5,param_1,param_2);
        puVar4 = param_1;
      }
    }
    else {
      uVar2 = (ulong)uVar1;
    }
  }
  if (lRam0000000002100880 == lStack_38) {
    return uVar2;
  }
  auVar5 = FUN_02091090(uVar2);
  uVar1 = auVar5._8_4_;
  lVar3 = auVar5._0_8_;
  if (puVar4[3] < (uVar1 & 0xff)) {
    uVar1 = 0x7246;
  }
  else {
    *(int *)((ulong)*puVar4 + lVar3) = 0x7f << (ulong)(puVar4[2] & 0x1f);
    if ((uVar1 & 0xff) != 0) {
      *(uint *)((ulong)puVar4[1] + lVar3) = (uVar1 & 0xff) << (ulong)(puVar4[2] & 0x1f);
    }
    uVar1 = 0;
    *(int *)((ulong)puVar4[4] + lVar3) = 1 << (ulong)(puVar4[5] & 0x1f);
  }
  return (ulong)uVar1;
}


