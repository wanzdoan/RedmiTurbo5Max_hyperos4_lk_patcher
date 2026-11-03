// ffff00009077b39c  size=292  FUN_ffff00009077b39c

undefined4 FUN_ffff00009077b39c(long param_1,undefined *param_2,ulong param_3,ulong param_4)

{
  undefined *puVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  undefined4 local_24;
  
  puVar1 = &DAT_ffff0000907df295;
  if (param_2 != (undefined *)0x0) {
    puVar1 = param_2;
  }
  if (param_1 != 0) {
    if (param_3 == 0) {
      return 0;
    }
    if ((((uint)param_4 | (uint)param_3) & 0xfff) == 0) {
      uVar4 = *(ulong *)(param_1 + 0x38);
      if ((param_4 < uVar4) || (lVar3 = *(long *)(param_1 + 0x40), (uVar4 + lVar3) - 1 < param_4)) {
        return 0xffffffdb;
      }
      uVar4 = param_4 - uVar4;
      uVar2 = -uVar4 - 2;
      if (!CARRY8(uVar4,param_3)) {
        uVar2 = param_3;
      }
      if (lVar3 - 1U <= uVar2 + uVar4) {
        uVar2 = lVar3 - uVar4;
      }
      FUN_ffff0000907175f4(&DAT_ffff000090832888,0xffffffff);
      local_24 = 0;
      FUN_ffff0000907087f8(param_1 + 0x58,param_4,0,&local_24);
      lVar3 = FUN_ffff00009077b4c0(param_1,puVar1,uVar2,param_4,0,1,1,local_24);
      FUN_ffff00009071768c(&DAT_ffff000090832888);
      if (lVar3 == 0) {
        return 0xfffffffb;
      }
      return 0;
    }
  }
  return 0xfffffff8;
}


