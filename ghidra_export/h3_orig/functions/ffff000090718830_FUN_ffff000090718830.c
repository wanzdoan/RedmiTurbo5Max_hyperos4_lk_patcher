// ffff000090718830  size=332  FUN_ffff000090718830

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff000090718830(undefined8 param_1,ulong param_2)

{
  undefined1 *puVar1;
  undefined8 *puVar2;
  int iVar3;
  int iVar4;
  undefined1 *puVar5;
  int iVar6;
  undefined4 uVar7;
  
  uVar7 = 0;
  puVar1 = (undefined1 *)0x0;
  if (_DAT_ffff000090843bc8 != &DAT_ffff000090843bc0) {
    puVar1 = _DAT_ffff000090843bc8;
  }
  puVar5 = puVar1 + -8;
  _DAT_ffff000090843b98 = 1;
  if (puVar1 != (undefined1 *)0x0 && puVar5 != (undefined1 *)0x0) {
    do {
      iVar6 = (int)param_2;
      if (iVar6 - *(int *)(puVar5 + 0x18) < 0) {
        FUN_ffff0000907166a8(FUN_ffff000090718830,0,*(int *)(puVar5 + 0x18) - iVar6);
        _DAT_ffff000090843b98 = 0;
        return uVar7;
      }
      puVar2 = *(undefined8 **)(puVar5 + 0x10);
      _DAT_ffff000090843b98 = 0;
      iVar3 = *(int *)(puVar5 + 0x1c);
      *puVar2 = *(undefined8 *)(puVar5 + 8);
      *(undefined8 **)(*(long *)(puVar5 + 8) + 8) = puVar2;
      *(undefined8 *)(puVar5 + 8) = 0;
      *(undefined8 *)(puVar5 + 0x10) = 0;
      iVar4 = (**(code **)(puVar5 + 0x20))(puVar5,param_2,*(undefined8 *)(puVar5 + 0x28));
      _DAT_ffff000090843b98 = 1;
      if (iVar4 == 1) {
        uVar7 = 1;
      }
      if ((((iVar3 != 0) && (*(long *)(puVar5 + 8) == 0)) && (*(long *)(puVar5 + 0x10) == 0)) &&
         (iVar3 = *(int *)(puVar5 + 0x1c), iVar3 != 0)) {
        iVar4 = *(int *)(puVar5 + 0x18) + iVar3;
        if (iVar4 - iVar6 < 0) {
          iVar4 = iVar3 + iVar6;
        }
        *(int *)(puVar5 + 0x18) = iVar4;
        FUN_ffff00009071899c();
      }
      puVar5 = (undefined1 *)0x0;
      if (_DAT_ffff000090843bc8 != &DAT_ffff000090843bc0) {
        puVar5 = _DAT_ffff000090843bc8;
      }
      if (puVar5 == (undefined1 *)0x0) {
        _DAT_ffff000090843b98 = 0;
        return uVar7;
      }
      puVar5 = puVar5 + -8;
      param_2 = param_2 & 0xffffffff;
    } while (puVar5 != (undefined1 *)0x0);
  }
  _DAT_ffff000090843b98 = 0;
  return uVar7;
}


