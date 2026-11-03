// ffff0000907b3944  size=768  FUN_ffff0000907b3944

undefined8
FUN_ffff0000907b3944
          (long param_1,long param_2,long param_3,long param_4,undefined1 *param_5,
          undefined1 *param_6,uint param_7)

{
  int iVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  
  iVar1 = FUN_ffff0000907acfd8(param_1,param_2,0x20);
  iVar2 = FUN_ffff0000907acfd8(param_3,param_4,0x20);
  uVar5 = (ulong)param_7;
  iVar3 = FUN_ffff0000907acfd8(param_5,param_6,uVar5);
  if ((iVar1 != 0) || (iVar3 != 0 || iVar2 != 0)) {
    if (iVar1 != 0 && (iVar3 != 0 || iVar2 != 0)) {
      FUN_ffff0000907acf74(param_1,param_2,0x20);
      FUN_ffff0000907acf74(param_3,param_4,0x20);
      FUN_ffff0000907acf74(param_5,param_6,uVar5);
      uVar4 = 0;
    }
    else {
      FUN_ffff00009077de54
                (s__s_last_stored_seal_cdi___size____ffff0000907ea2b4,s__DICE__ffff0000907f547f,0x20
                );
      lVar7 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)(param_1 + lVar7));
        FUN_ffff00009077cbd8(1);
        lVar7 = lVar7 + 1;
      } while (lVar7 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      FUN_ffff00009077de54
                (s__s_current_seal_cdi___size___d___ffff000090801968,s__DICE__ffff0000907f547f,0x20)
      ;
      lVar7 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)(param_2 + lVar7));
        FUN_ffff00009077cbd8(1);
        lVar7 = lVar7 + 1;
      } while (lVar7 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      FUN_ffff00009077de54
                (s__s_last_stored_previous_seal_cdi_ffff0000907f8f67,s__DICE__ffff0000907f547f,0x20)
      ;
      lVar7 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)(param_3 + lVar7));
        FUN_ffff00009077cbd8(1);
        lVar7 = lVar7 + 1;
      } while (lVar7 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      FUN_ffff00009077de54
                (s__s_current_previous_seal_cdi___s_ffff0000907ef02a,s__DICE__ffff0000907f547f,0x20)
      ;
      lVar7 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)(param_4 + lVar7));
        FUN_ffff00009077cbd8(1);
        lVar7 = lVar7 + 1;
      } while (lVar7 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      FUN_ffff00009077de54
                (s__s_last_stored_input_data_hash____ffff0000907fe4af,s__DICE__ffff0000907f547f,
                 param_7);
      uVar6 = uVar5;
      if (param_7 == 0) {
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        FUN_ffff00009077de54
                  (s__s_current_input_data_hash___siz_ffff0000907e220a,s__DICE__ffff0000907f547f,0);
      }
      else {
        do {
          FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*param_5);
          FUN_ffff00009077cbd8(1);
          uVar6 = uVar6 - 1;
          param_5 = param_5 + 1;
        } while (uVar6 != 0);
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        FUN_ffff00009077de54
                  (s__s_current_input_data_hash___siz_ffff0000907e220a,s__DICE__ffff0000907f547f,
                   param_7);
        if (param_7 != 0) {
          do {
            FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*param_6);
            FUN_ffff00009077cbd8(1);
            uVar5 = uVar5 - 1;
            param_6 = param_6 + 1;
          } while (uVar5 != 0);
        }
      }
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      uVar4 = 0x10d012;
    }
  }
  else {
    uVar4 = 0;
  }
  return uVar4;
}


