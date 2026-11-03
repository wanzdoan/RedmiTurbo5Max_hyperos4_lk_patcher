
uint FUN_ffff0000b80044a8(long param_1,long param_2,uint *param_3,char param_4,int param_5)

{
  byte bVar1;
  uint uVar2;
  uint uVar3;
  uint local_28;
  byte local_24 [4];
  
  if (*(byte *)(param_2 + 0x30) < 0x20) {
    local_28 = 0;
    local_24[0] = (byte)*(undefined4 *)(param_2 + 0x38) | param_4 << 3;
    uVar2 = (**(code **)(param_1 + 0x20))(param_1,*(undefined4 *)(param_2 + 0x34),local_24,1);
    local_24[0] = (byte)*(undefined4 *)(param_2 + 0x20);
    uVar3 = (**(code **)(param_1 + 0x20))(param_1,*(undefined4 *)(param_2 + 0x1c),local_24,1);
    uVar3 = uVar3 | uVar2;
    if (-1 < (int)uVar3) {
      FUN_ffff0000b804b704(*(undefined4 *)(param_2 + 0x14));
      uVar2 = *(uint *)(param_2 + 0x14);
      while( true ) {
        uVar3 = (**(code **)(param_1 + 0x28))(param_1,*(undefined4 *)(param_2 + 0x24),&local_28,2);
        if ((-1 < (int)uVar3) && ((*(uint *)(param_2 + 0x28) & local_28) != 0)) {
          bVar1 = *(byte *)(param_2 + 0x30);
          uVar3 = 0xffffffff;
          if (bVar1 < 0x40) {
            uVar3 = ~(uint)(-1L << ((ulong)bVar1 & 0x3f));
          }
          if (param_5 != 0) {
            if (param_5 == 1) {
              *param_3 = uVar3 & local_28;
              return 0;
            }
            return 0;
          }
          uVar2 = 0;
          if (*(uint *)(param_2 + 0xc) != 0) {
            uVar2 = (*(int *)(param_2 + 8) * (uVar3 & local_28) * *(int *)(param_2 + 4)) /
                    *(uint *)(param_2 + 0xc);
          }
          *param_3 = uVar2 >> (ulong)(bVar1 & 0x1f);
          return 0;
        }
        if (*(uint *)(param_2 + 0x10) < uVar2) break;
        FUN_ffff0000b804b704(*(undefined4 *)(param_2 + 0x18));
        uVar2 = *(int *)(param_2 + 0x18) + uVar2;
      }
      if (-1 < (int)uVar3) {
        uVar3 = 0xfffffff3;
      }
    }
  }
  else {
    uVar3 = 0xfffffff8;
  }
  return uVar3;
}


