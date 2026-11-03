
undefined4 FUN_ffff0000b8048d04(uint *param_1,ulong param_2,ulong param_3)

{
  ulong uVar1;
  ulong *puVar2;
  ulong uVar3;
  undefined4 uVar4;
  uint uVar5;
  ulong uVar6;
  
  uVar5 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
  if (uVar5 == 0x2ff20112) {
    uVar5 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    if (uVar5 >> 0x10 == 0 && (uVar5 & 0xffff) == 0) {
      uVar6 = (long)(int)((uint)(byte)param_1[2] << 0x18) |
              (ulong)*(byte *)((long)param_1 + 9) << 0x10 |
              (ulong)*(byte *)((long)param_1 + 10) << 8 | (ulong)*(byte *)((long)param_1 + 0xb);
      uVar5 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
      uVar1 = uVar6 + 0x10;
      if ((uVar5 >> 0x10 | uVar5 << 0x10) < uVar1) {
        uVar4 = 0xfffffffd;
      }
      else {
        uVar4 = 0;
        puVar2 = (ulong *)((long)param_1 + uVar6);
        uVar6 = (param_2 & 0xff00ff00ff00ff00) >> 8 | (param_2 & 0xff00ff00ff00ff) << 8;
        uVar6 = (uVar6 & 0xffff0000ffff0000) >> 0x10 | (uVar6 & 0xffff0000ffff) << 0x10;
        uVar3 = (param_3 & 0xff00ff00ff00ff00) >> 8 | (param_3 & 0xff00ff00ff00ff) << 8;
        uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
        uVar5 = (uint)uVar1;
        uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
        *puVar2 = uVar6 >> 0x20 | uVar6 << 0x20;
        puVar2[1] = uVar3 >> 0x20 | uVar3 << 0x20;
        param_1[2] = uVar5 >> 0x10 | uVar5 << 0x10;
      }
    }
    else {
      uVar4 = 0xfffffff9;
    }
  }
  else {
    uVar4 = 0xfffffff9;
    if (uVar5 != 0xd00dfeed) {
      uVar4 = 0xfffffff7;
    }
  }
  return uVar4;
}


