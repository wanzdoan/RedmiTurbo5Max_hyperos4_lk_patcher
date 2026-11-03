// ffff00009070ee04  size=1048  FUN_ffff00009070ee04

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070ee04(int *param_1,undefined4 *param_2,uint param_3)

{
  undefined4 *puVar1;
  undefined4 uVar2;
  long lVar3;
  ulong uVar4;
  int iVar5;
  long lVar6;
  ulong uVar7;
  
  *(undefined4 **)(param_1 + 4) = param_2;
  param_1[2] = param_3;
LAB_ffff00009070eea8:
  do {
    if (param_3 == 0) break;
    switch(param_1[1]) {
    case 0:
      if (param_3 < 0x1c) goto code_r0xffff00009070ee9c;
      FUN_ffff00009077eb88(0xffff00009083dfa8,0,0x50);
      FUN_ffff00009077e858(0xffff00009083dfa8,param_2,0x1c);
      _DAT_ffff00009083dfe8 = 0;
      _DAT_ffff00009083dfd0 = 0;
      param_3 = param_3 - _DAT_ffff00009083dfb0;
      param_2 = (undefined4 *)((long)param_2 + (ulong)_DAT_ffff00009083dfb0);
      goto LAB_ffff00009070f08c;
    case 1:
      if (_DAT_ffff00009083dfbc <= _DAT_ffff00009083dfd0) {
        param_1[2] = 0;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (_DAT_ffff00009083dfe8 + param_3 < (uint)_DAT_ffff00009083dfb2) {
        _DAT_ffff00009083dfe8 = param_3;
        FUN_ffff00009077e858(0xffff00009083dfec,param_2,param_3);
        param_1[2] = 0;
        *(undefined4 **)(param_1 + 4) = param_2;
        param_1[0] = 0;
        param_1[1] = 1;
        return;
      }
      if (_DAT_ffff00009083dfe8 == 0) {
        FUN_ffff00009077e858(&DAT_ffff00009083dfc4,param_2,0xc);
        uVar7 = (ulong)_DAT_ffff00009083dfb2;
      }
      else {
        uVar7 = (ulong)(_DAT_ffff00009083dfb2 - _DAT_ffff00009083dfe8);
        FUN_ffff00009077e858(&DAT_ffff00009083dfc4,0xffff00009083dfec);
        uVar4 = uVar7;
        if (0xc < (ulong)_DAT_ffff00009083dfb2) {
          uVar4 = (uVar7 - _DAT_ffff00009083dfb2) + 0xc;
        }
        FUN_ffff00009077e858(&DAT_ffff00009083dfc4 + _DAT_ffff00009083dfe8,param_2,uVar4);
        _DAT_ffff00009083dfe8 = 0;
      }
      _DAT_ffff00009083dfd8 = (ulong)_DAT_ffff00009083dfb4 * (ulong)_DAT_ffff00009083dfc8;
      if (3 < _DAT_ffff00009083dfc4 - 0xcac1) {
        iVar5 = -0x6a;
        goto LAB_ffff00009070f1a4;
      }
      param_3 = param_3 - (int)uVar7;
      iVar5 = 2;
      switch((uint)_DAT_ffff00009083dfc4) {
      case 0xcac2:
        iVar5 = 4;
        break;
      case 0xcac3:
        _DAT_ffff00009083dfe0 = _DAT_ffff00009083dfe0 + _DAT_ffff00009083dfd8;
        iVar5 = 1;
        _DAT_ffff00009083dfd8 = 0;
        break;
      case 0xcac4:
        iVar5 = 5;
      }
      param_2 = (undefined4 *)((long)param_2 + uVar7);
      *param_1 = 0;
      param_1[1] = iVar5;
      param_1[2] = param_3;
      *(undefined4 **)(param_1 + 4) = param_2;
      _DAT_ffff00009083dfd0 = _DAT_ffff00009083dfd0 + 1;
      goto code_r0xffff00009070ee9c;
    case 2:
      uVar7 = (ulong)param_3;
      if (uVar7 < _DAT_ffff00009083dfd8) {
        if ((param_3 & 0xfff) != 0) {
          if (param_3 < 0x1000) {
            param_1[0] = 100;
            param_1[1] = 2;
            return;
          }
          uVar7 = (ulong)(param_3 & 0xfffff000);
        }
        uVar4 = FUN_ffff00009077be7c
                          (**(undefined8 **)(param_1 + 10),param_2,_DAT_ffff00009083dfe0,uVar7);
        if (uVar4 != uVar7) {
LAB_ffff00009070f1a0:
          iVar5 = -0x67;
LAB_ffff00009070f1a4:
          *param_1 = iVar5;
          return;
        }
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        _DAT_ffff00009083dfd8 = _DAT_ffff00009083dfd8 - uVar7;
        _DAT_ffff00009083dfe0 = _DAT_ffff00009083dfe0 + uVar7;
        iVar5 = 2;
        param_3 = param_3 & 0xfff;
      }
      else {
        uVar7 = FUN_ffff00009077be7c(**(undefined8 **)(param_1 + 10),param_2,_DAT_ffff00009083dfe0);
        if (uVar7 != _DAT_ffff00009083dfd8) goto LAB_ffff00009070f1a0;
        if (*param_1 != 0) {
          return;
        }
        param_2 = (undefined4 *)((long)param_2 + uVar7);
        param_3 = param_3 - (int)uVar7;
        _DAT_ffff00009083dfe0 = _DAT_ffff00009083dfe0 + uVar7;
        iVar5 = 1;
        _DAT_ffff00009083dfd8 = 0;
      }
      break;
    default:
      goto code_r0xffff00009070ee9c;
    case 4:
      uVar2 = *param_2;
      lVar3 = FUN_ffff00009070f250();
      lVar6 = 0;
      do {
        puVar1 = (undefined4 *)(lVar3 + lVar6);
        lVar6 = lVar6 + 8;
        *puVar1 = uVar2;
        puVar1[1] = uVar2;
      } while (lVar6 != 0x200000);
      param_2 = param_2 + 1;
      param_3 = param_3 - 4;
      uVar7 = _DAT_ffff00009083dfd8;
      if (0x1fffff < _DAT_ffff00009083dfd8) {
        uVar7 = 0x200000;
      }
      while (uVar7 != 0) {
        uVar4 = FUN_ffff00009077be7c
                          (**(undefined8 **)(param_1 + 10),lVar3,_DAT_ffff00009083dfe0,uVar7);
        if (uVar4 != uVar7) goto LAB_ffff00009070f1a0;
        if (*param_1 != 0) {
          return;
        }
        _DAT_ffff00009083dfd8 = _DAT_ffff00009083dfd8 - uVar7;
        _DAT_ffff00009083dfe0 = _DAT_ffff00009083dfe0 + uVar7;
        uVar7 = _DAT_ffff00009083dfd8;
        if (0x1fffff < _DAT_ffff00009083dfd8) {
          uVar7 = 0x200000;
        }
      }
LAB_ffff00009070f08c:
      iVar5 = 1;
      break;
    case 5:
      param_2 = param_2 + 1;
      iVar5 = 1;
      _DAT_ffff00009083dfd8 = 0;
      param_3 = param_3 - 4;
    }
    *param_1 = 0;
    param_1[1] = iVar5;
    param_1[2] = param_3;
    *(undefined4 **)(param_1 + 4) = param_2;
    if (param_3 == 0) {
      return;
    }
  } while( true );
code_r0xffff00009070ee9c:
  if (*param_1 != 0) {
    return;
  }
  if (param_3 == 0) {
    return;
  }
  goto LAB_ffff00009070eea8;
}


