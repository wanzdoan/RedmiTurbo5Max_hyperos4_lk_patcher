
void FUN_ffff0000be097ec8(long param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  undefined8 *puVar5;
  int local_54;
  uint local_38;
  uint local_34;
  
  uVar4 = (ulong)*(uint *)(param_1 + 0x18);
  if (0 < (int)*(uint *)(param_1 + 0x18)) {
    puVar5 = (undefined8 *)(*(long *)(param_1 + 0x20) + 8);
    do {
      uVar3 = *puVar5;
      local_54 = 0;
      lVar2 = FUN_ffff0000be0976e4(uVar3,s_phandle_ffff0000be0bc639,&local_54);
      if ((lVar2 != 0) && (local_54 == 4)) {
        local_38 = 0;
        thunk_FUN_ffff0000be0607b0(&local_38,lVar2,4);
        uVar1 = (local_38 & 0xff00ff00) >> 8 | (local_38 & 0xff00ff) << 8;
        uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + param_2;
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        local_38 = uVar1 >> 0x10 | uVar1 << 0x10;
        thunk_FUN_ffff0000be0607b0(lVar2,&local_38,4);
      }
      lVar2 = FUN_ffff0000be0976e4(uVar3,s_linux_phandle_ffff0000be0a875b,&local_54);
      if ((lVar2 != 0) && (local_54 == 4)) {
        local_34 = 0;
        thunk_FUN_ffff0000be0607b0(&local_34,lVar2,4);
        uVar1 = (local_34 & 0xff00ff00) >> 8 | (local_34 & 0xff00ff) << 8;
        uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + param_2;
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        local_34 = uVar1 >> 0x10 | uVar1 << 0x10;
        thunk_FUN_ffff0000be0607b0(lVar2,&local_34,4);
      }
      puVar5 = puVar5 + 2;
      uVar4 = uVar4 - 1;
    } while (uVar4 != 0);
  }
  return;
}


