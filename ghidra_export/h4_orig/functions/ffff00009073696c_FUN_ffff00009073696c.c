// ffff00009073696c  size=396  FUN_ffff00009073696c

ulong FUN_ffff00009073696c(long param_1,long param_2,uint *param_3,int param_4)

{
  ulong uVar1;
  byte bVar2;
  uint uVar3;
  ulong uVar4;
  uint uVar5;
  uint local_44;
  
  bVar2 = *(byte *)(param_2 + 0x30);
  if ((ulong)bVar2 < 0x20) {
    if (*(int *)(param_2 + 0x34) != 0) {
      (**(code **)(param_1 + 0x20))(param_1,*(int *)(param_2 + 0x34),param_2 + 0x38,1);
    }
    uVar4 = (**(code **)(param_1 + 0x20))(param_1,*(undefined4 *)(param_2 + 0x1c),param_2 + 0x20,1);
    if (-1 < (int)uVar4) {
      uVar4 = (ulong)bVar2 + 7 >> 3;
      local_44 = 0;
      FUN_ffff00009077cea0(*(undefined4 *)(param_2 + 0x14));
      uVar5 = *(uint *)(param_2 + 0x14);
      while( true ) {
        uVar1 = uVar4;
        if (*(int *)(param_2 + 0x24) != *(int *)(param_2 + 0x2c)) {
          uVar1 = 1;
        }
        uVar3 = (**(code **)(param_1 + 0x28))(param_1,*(int *)(param_2 + 0x24),&local_44,uVar1);
        if ((-1 < (int)uVar3) && ((*(uint *)(param_2 + 0x28) & local_44) != 0)) {
          if ((*(int *)(param_2 + 0x24) != *(int *)(param_2 + 0x2c)) &&
             (uVar4 = (**(code **)(param_1 + 0x28))
                                (param_1,*(int *)(param_2 + 0x2c),&local_44,uVar4), (int)uVar4 < 0))
          {
            return uVar4;
          }
          bVar2 = *(byte *)(param_2 + 0x30);
          uVar5 = 0xffffffff;
          if (bVar2 < 0x40) {
            uVar5 = ~(uint)(-1L << ((ulong)bVar2 & 0x3f));
          }
          uVar5 = uVar5 & local_44;
          if (param_4 == 0) {
            uVar3 = 0;
            if (*(uint *)(param_2 + 0xc) != 0) {
              uVar3 = (*(int *)(param_2 + 8) * uVar5 * *(int *)(param_2 + 4)) /
                      *(uint *)(param_2 + 0xc);
            }
            uVar5 = uVar3 >> (ulong)(bVar2 & 0x1f);
          }
          else if (param_4 != 1) {
            return 0;
          }
          *param_3 = uVar5;
          return 0;
        }
        if (*(uint *)(param_2 + 0x10) < uVar5) break;
        FUN_ffff00009077cea0(*(undefined4 *)(param_2 + 0x18));
        uVar5 = *(int *)(param_2 + 0x18) + uVar5;
      }
      if (-1 < (int)uVar3) {
        uVar3 = 0xfffffff3;
      }
      uVar4 = (ulong)uVar3;
    }
  }
  else {
    uVar4 = 0xfffffff8;
  }
  return uVar4;
}


