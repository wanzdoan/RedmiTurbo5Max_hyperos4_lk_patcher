// ffff0000907c86a4  size=1024  FUN_ffff0000907c86a4

void FUN_ffff0000907c86a4(ulong param_1,uint *param_2,uint *param_3,uint param_4)

{
  uint *puVar1;
  uint uVar2;
  uint *puVar3;
  uint uVar4;
  uint uVar5;
  
  if (param_1 < 0x10) {
    uVar4 = 0;
  }
  else {
    uVar4 = 0;
    puVar3 = param_3;
    do {
      param_1 = param_1 - 0x10;
      uVar5 = (uint)((ulong)*param_2 * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)*param_2 * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,*puVar3);
      *puVar3 = uVar2 + *puVar3;
      uVar5 = (uint)((ulong)param_2[1] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[1] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[1]);
      puVar3[1] = uVar2 + puVar3[1];
      uVar5 = (uint)((ulong)param_2[2] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[2] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[2]);
      puVar3[2] = uVar2 + puVar3[2];
      uVar5 = (uint)((ulong)param_2[3] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[3] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[3]);
      puVar3[3] = uVar2 + puVar3[3];
      uVar5 = (uint)((ulong)param_2[4] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[4] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[4]);
      puVar3[4] = uVar2 + puVar3[4];
      uVar5 = (uint)((ulong)param_2[5] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[5] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[5]);
      puVar3[5] = uVar2 + puVar3[5];
      uVar5 = (uint)((ulong)param_2[6] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[6] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[6]);
      puVar3[6] = uVar2 + puVar3[6];
      uVar5 = (uint)((ulong)param_2[7] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[7] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[7]);
      puVar3[7] = uVar2 + puVar3[7];
      uVar5 = (uint)((ulong)param_2[8] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[8] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[8]);
      puVar3[8] = uVar2 + puVar3[8];
      uVar5 = (uint)((ulong)param_2[9] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[9] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[9]);
      puVar3[9] = uVar2 + puVar3[9];
      uVar5 = (uint)((ulong)param_2[10] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[10] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[10]);
      puVar3[10] = uVar2 + puVar3[10];
      uVar5 = (uint)((ulong)param_2[0xb] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[0xb] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[0xb]);
      puVar3[0xb] = uVar2 + puVar3[0xb];
      uVar5 = (uint)((ulong)param_2[0xc] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[0xc] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[0xc]);
      puVar3[0xc] = uVar2 + puVar3[0xc];
      uVar5 = (uint)((ulong)param_2[0xd] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[0xd] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[0xd]);
      puVar3[0xd] = uVar2 + puVar3[0xd];
      uVar5 = (uint)((ulong)param_2[0xe] * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      uVar4 = (int)((ulong)param_2[0xe] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[0xe]);
      puVar3[0xe] = uVar2 + puVar3[0xe];
      puVar1 = param_2 + 0xf;
      uVar5 = (uint)((ulong)*puVar1 * (ulong)param_4);
      uVar2 = uVar4 + uVar5;
      param_2 = param_2 + 0x10;
      param_3 = puVar3 + 0x10;
      uVar4 = (int)((ulong)*puVar1 * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
              (uint)CARRY4(uVar2,puVar3[0xf]);
      puVar3[0xf] = uVar2 + puVar3[0xf];
      puVar3 = param_3;
    } while (0xf < param_1);
  }
  for (; 7 < param_1; param_1 = param_1 - 8) {
    uVar5 = (uint)((ulong)*param_2 * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)*param_2 * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,*param_3);
    *param_3 = uVar2 + *param_3;
    uVar5 = (uint)((ulong)param_2[1] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[1] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[1]);
    param_3[1] = uVar2 + param_3[1];
    uVar5 = (uint)((ulong)param_2[2] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[2] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[2]);
    param_3[2] = uVar2 + param_3[2];
    uVar5 = (uint)((ulong)param_2[3] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[3] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[3]);
    param_3[3] = uVar2 + param_3[3];
    uVar5 = (uint)((ulong)param_2[4] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[4] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[4]);
    param_3[4] = uVar2 + param_3[4];
    uVar5 = (uint)((ulong)param_2[5] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[5] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[5]);
    param_3[5] = uVar2 + param_3[5];
    uVar5 = (uint)((ulong)param_2[6] * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)param_2[6] * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[6]);
    param_3[6] = uVar2 + param_3[6];
    puVar3 = param_2 + 7;
    uVar5 = (uint)((ulong)*puVar3 * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    param_2 = param_2 + 8;
    uVar4 = (int)((ulong)*puVar3 * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,param_3[7]);
    param_3[7] = uVar2 + param_3[7];
    param_3 = param_3 + 8;
  }
  for (; param_1 != 0; param_1 = param_1 - 1) {
    uVar5 = (uint)((ulong)*param_2 * (ulong)param_4);
    uVar2 = uVar4 + uVar5;
    uVar4 = (int)((ulong)*param_2 * (ulong)param_4 >> 0x20) + (uint)CARRY4(uVar4,uVar5) +
            (uint)CARRY4(uVar2,*param_3);
    *param_3 = uVar2 + *param_3;
    param_2 = param_2 + 1;
    param_3 = param_3 + 1;
  }
  uVar2 = *param_3;
  *param_3 = uVar2 + uVar4;
  if (CARRY4(uVar2,uVar4)) {
    do {
      param_3 = param_3 + 1;
      uVar4 = *param_3;
      *param_3 = uVar4 + 1;
    } while (0xfffffffe < uVar4);
  }
  return;
}


