// ffff000090729d8c  size=172  FUN_ffff000090729d8c

undefined8 FUN_ffff000090729d8c(undefined1 *param_1,ulong param_2,long param_3,ulong *param_4)

{
  ulong uVar1;
  undefined1 *puVar2;
  int iVar3;
  undefined8 uVar4;
  
  uVar4 = 0xfffffff8;
  if ((((param_1 != (undefined1 *)0x0) && (param_2 < 0x3f0)) && (param_3 != 0)) &&
     (param_4 != (ulong *)0x0)) {
    puVar2 = param_1;
    uVar1 = param_2;
    if (*param_4 < param_2) {
      uVar4 = 0xfffffff7;
    }
    else {
      do {
        uVar1 = uVar1 - 1;
        if (uVar1 == 0) {
          FUN_ffff00009077e858(param_3,param_1,param_2);
          *param_4 = param_2;
          return 0;
        }
        iVar3 = FUN_ffff00009077d8bc(*puVar2);
        puVar2 = puVar2 + 1;
      } while (iVar3 != 0);
      uVar4 = 0xfffffff9;
    }
  }
  return uVar4;
}


