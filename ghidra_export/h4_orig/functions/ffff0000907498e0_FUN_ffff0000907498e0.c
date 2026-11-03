// ffff0000907498e0  size=288  FUN_ffff0000907498e0

void FUN_ffff0000907498e0(uint param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 *puVar6;
  
  uVar3 = DAT_ffff00009081a74c;
  FUN_ffff00009077e11c
            (s__DCM__WARN____s_type_0x_X__set___ffff0000907eb14a,s_dcm_set_state_ffff0000907cfa79,
             param_1,0,DAT_ffff00009081a74c);
  if (param_1 != 0) {
    puVar6 = &DAT_ffff00009081a750;
    do {
      lVar4 = *(long *)(puVar6 + 0xc);
      if (lVar4 == 0) break;
      uVar1 = puVar6[10];
      if ((uVar1 & param_1) != 0) {
        iVar5 = puVar6[2];
        puVar6[1] = 0;
        uVar2 = uVar1;
        if (iVar5 == 0) {
          *puVar6 = 0;
          DAT_ffff00009081a74c = DAT_ffff00009081a74c & ~uVar1;
          (**(code **)(puVar6 + 4))(0);
          lVar4 = *(long *)(puVar6 + 0xc);
          iVar5 = puVar6[2];
          uVar2 = puVar6[10];
        }
        param_1 = param_1 & (uVar1 ^ 0xffffffff);
        FUN_ffff00009077e11c
                  (s__DCM__INFO____s_0x_X__current_st_ffff0000907dfd08,lVar4,uVar2,*puVar6,iVar5);
      }
      puVar6 = puVar6 + 0xe;
    } while (param_1 != 0);
  }
  if (uVar3 != DAT_ffff00009081a74c) {
    FUN_ffff00009077e11c
              (s__DCM__WARN____s_type_0x_X__set___ffff0000907e31c2,s_dcm_set_state_ffff0000907cfa79,
               param_1,0,uVar3);
    return;
  }
  return;
}


