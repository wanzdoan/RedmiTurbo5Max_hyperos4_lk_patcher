
int FUN_ffff0000907a08f4
              (long param_1,uint param_2,long param_3,uint *param_4,long param_5,uint param_6,
              long param_7,uint param_8)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  int iVar4;
  undefined8 *puVar5;
  undefined8 *puVar6;
  ulong uVar7;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  iVar2 = 0x102012;
  iVar1 = 0x102012;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  if (((((param_1 != 0) && (param_3 != 0)) && (param_4 != (uint *)0x0)) &&
      ((param_5 != 0 && (iVar2 = iVar1, param_6 != 0)))) && ((param_7 != 0 && (param_8 != 0)))) {
    if (-1 < (int)(param_6 | param_2 | param_8)) {
      iVar1 = FUN_ffff00009079c104(&local_78);
      iVar2 = 0x102014;
      if (iVar1 == 0) {
        iVar2 = FUN_ffff00009079c104(&uStack_90);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff00009079c104(&local_a8);
          if (iVar2 == 0) {
            iVar2 = FUN_ffff00009079f2a8(&local_78,param_1,param_2);
            if (((iVar2 == 0) &&
                (iVar2 = FUN_ffff00009079f2a8(&uStack_90,param_5,param_6), iVar2 == 0)) &&
               (iVar2 = FUN_ffff00009079f2a8(&local_a8,param_7,param_8), iVar2 == 0)) {
              iVar2 = FUN_ffff00009079f1d4(&local_a8,&local_78);
              if (iVar2 == -1) {
                iVar2 = 0x102013;
              }
              else {
                iVar2 = FUN_ffff00009079e778(&local_78,&uStack_90,&local_a8,&local_78);
                if (iVar2 == 0) {
                  uVar3 = FUN_ffff00009079f454(&local_a8);
                  uVar7 = (ulong)(int)uVar3;
                  if (*param_4 < uVar7) {
                    *param_4 = uVar3;
                    iVar2 = 0x102013;
                  }
                  else {
                    iVar1 = FUN_ffff00009079f454(&local_78);
                    iVar4 = FUN_ffff00009079f454(&local_a8);
                    iVar2 = 0x102013;
                    if (iVar1 <= iVar4) {
                      *param_4 = uVar3;
                      FUN_ffff00009077ee50(param_3,0,uVar7);
                      iVar2 = FUN_ffff00009079f454(&local_78);
                      iVar2 = FUN_ffff00009079f4c8(&local_78,(param_3 + uVar7) - (long)iVar2);
                    }
                  }
                }
              }
            }
            FUN_ffff00009079c17c(&local_78);
            puVar6 = &local_a8;
            puVar5 = &uStack_90;
          }
          else {
            iVar2 = 0x102014;
            puVar6 = &uStack_90;
            puVar5 = &local_78;
          }
          FUN_ffff00009079c17c(puVar5);
        }
        else {
          puVar6 = &local_78;
          iVar2 = 0x102014;
        }
        FUN_ffff00009079c17c(puVar6);
      }
    }
  }
  return iVar2;
}


