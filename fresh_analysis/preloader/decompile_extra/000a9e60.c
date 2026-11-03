// File offset entry: 0xa9e60

undefined8 FUN_020a9d60(long param_1,uint *param_2,uint *param_3)

{
  uint uVar1;
  undefined8 uVar2;
  uint *puVar3;
  uint uVar4;
  uint uVar5;
  
  uVar5 = *param_3 >> 0x1e;
  if ((uVar5 == 0) || ((*param_3 & 0x780000) != 0x80000)) {
    uVar5 = 0;
    uVar2 = 4;
  }
  else {
    if (uVar5 == 2) {
      uVar4 = 0;
      *(undefined4 *)(param_1 + 0x104) = 0;
      *(undefined4 *)(param_1 + 0x104) = 0;
    }
    else {
      uVar5 = (*param_2 & 0xff00ff00) >> 8 | (*param_2 & 0xff00ff) << 8;
      *(uint *)(param_1 + 0x104) = uVar5 >> 0x10 | uVar5 << 0x10;
      uVar5 = (param_2[1] & 0xff00ff00) >> 8 | (param_2[1] & 0xff00ff) << 8;
      *(uint *)(param_1 + 0x104) = uVar5 >> 0x10 | uVar5 << 0x10;
      uVar4 = 8;
    }
    uVar5 = uVar4 + 8;
    uVar2 = 8;
    uVar1 = (*(uint *)((long)param_2 + (ulong)uVar4) & 0xff00ff00) >> 8 |
            (*(uint *)((long)param_2 + (ulong)uVar4) & 0xff00ff) << 8;
    *(uint *)(param_1 + 0x104) = uVar1 >> 0x10 | uVar1 << 0x10;
    uVar4 = *(uint *)(((ulong)uVar4 | 4) + (long)param_2);
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    *(uint *)(param_1 + 0x104) = uVar4 >> 0x10 | uVar4 << 0x10;
  }
  puVar3 = (uint *)(param_1 + 0x104);
  param_2 = (uint *)((long)param_2 + (ulong)uVar5);
  uVar5 = (*param_2 & 0xff00ff00) >> 8 | (*param_2 & 0xff00ff) << 8;
  *puVar3 = uVar5 >> 0x10 | uVar5 << 0x10;
  uVar5 = (param_2[1] & 0xff00ff00) >> 8 | (param_2[1] & 0xff00ff) << 8;
  *puVar3 = uVar5 >> 0x10 | uVar5 << 0x10;
  uVar5 = (param_2[2] & 0xff00ff00) >> 8 | (param_2[2] & 0xff00ff) << 8;
  *puVar3 = uVar5 >> 0x10 | uVar5 << 0x10;
  uVar5 = (param_2[3] & 0xff00ff00) >> 8 | (param_2[3] & 0xff00ff) << 8;
  *puVar3 = uVar5 >> 0x10 | uVar5 << 0x10;
  return uVar2;
}


