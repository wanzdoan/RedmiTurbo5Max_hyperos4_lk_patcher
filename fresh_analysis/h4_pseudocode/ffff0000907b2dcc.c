
undefined4 FUN_ffff0000907b2dcc(long param_1,undefined4 param_2,long param_3,uint param_4)

{
  undefined1 uVar1;
  undefined *puVar2;
  int iVar3;
  undefined4 uVar4;
  ulong uVar5;
  uint uVar6;
  undefined1 *puVar7;
  undefined1 local_4c0 [512];
  undefined1 local_2c0 [560];
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 local_58;
  int local_34;
  
  local_34 = 0;
  local_58 = 0;
  local_60 = 0;
  local_68 = 0;
  local_70 = 0;
  local_78 = 0;
  local_80 = 0;
  local_88 = 0;
  local_90 = 0;
  FUN_ffff00009077ee50(local_2c0,0,0x200);
  FUN_ffff00009077ee50(local_4c0,0,0x200);
  FUN_ffff00009077e11c(s___s__SW__Cert_auth__ffff0000907f59e4,&DAT_ffff0000907f228a);
  puVar2 = PTR_DAT_ffff000090839ae8;
  uVar4 = 0x100000;
  if ((param_1 != 0) && (param_3 != 0)) {
    if (DAT_ffff000090837ec0 == param_4) {
      iVar3 = FUN_ffff0000907a29bc
                        (param_1,param_2,&local_90,*(undefined4 *)PTR_DAT_ffff000090839ae8);
      if (iVar3 == 0) {
        FUN_ffff0000907ad234(local_2c0,param_3,param_4);
        if (1 < DAT_ffff000090837ec0) {
          uVar5 = (ulong)(DAT_ffff000090837ec0 >> 1);
          puVar7 = local_2c0;
          uVar6 = DAT_ffff000090837ec0;
          do {
            uVar6 = uVar6 - 1;
            uVar5 = uVar5 - 1;
            uVar1 = *puVar7;
            *puVar7 = local_2c0[uVar6];
            local_2c0[uVar6] = uVar1;
            puVar7 = puVar7 + 1;
          } while (uVar5 != 0);
        }
        FUN_ffff0000907a2a34(local_2c0,&DAT_ffff00009090ccc0,local_4c0,*(undefined4 *)puVar2);
        if (1 < DAT_ffff000090837ec0) {
          uVar5 = (ulong)(DAT_ffff000090837ec0 >> 1);
          puVar7 = local_4c0;
          uVar6 = DAT_ffff000090837ec0;
          do {
            uVar6 = uVar6 - 1;
            uVar5 = uVar5 - 1;
            uVar1 = *puVar7;
            *puVar7 = local_4c0[uVar6];
            local_4c0[uVar6] = uVar1;
            puVar7 = puVar7 + 1;
          } while (uVar5 != 0);
        }
        iVar3 = FUN_ffff0000907a2abc(&local_90,local_4c0,&local_34,*(undefined4 *)puVar2);
        if (iVar3 == 0) {
          uVar4 = 0;
          if (local_34 != 1) {
            uVar4 = 0x701f;
          }
        }
        else {
          uVar4 = 0x701e;
        }
      }
      else {
        uVar4 = 0x701d;
      }
    }
    else {
      uVar4 = 0x7022;
    }
  }
  return uVar4;
}


