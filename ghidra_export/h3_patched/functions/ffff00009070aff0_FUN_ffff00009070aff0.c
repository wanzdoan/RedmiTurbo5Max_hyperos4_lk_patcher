// ffff00009070aff0  size=372  FUN_ffff00009070aff0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070aff0(undefined8 param_1)

{
  long *plVar1;
  int iVar2;
  uint uVar3;
  undefined *puVar4;
  long lVar5;
  char *pcVar6;
  undefined8 *puVar7;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined1 local_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  iVar2 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907f79ce);
  plVar1 = _DAT_ffff00009083d168;
  if (iVar2 == 0) {
    for (; plVar1 != (long *)0x0; plVar1 = (long *)*plVar1) {
      iVar2 = FUN_ffff00009077f434(&local_60,0x40,s__s___s_ffff0000907ea533,plVar1[1],plVar1[2]);
      if (-1 < iVar2) {
        FUN_ffff000090709c60(&local_60);
      }
    }
    pcVar6 = s_Done___ffff0000907ea53b;
  }
  else {
    puVar7 = (undefined8 *)&DAT_ffff00009083d168;
    do {
      puVar7 = (undefined8 *)*puVar7;
      if (puVar7 == (undefined8 *)0x0) {
        puVar4 = &DAT_ffff0000907df27e;
        goto LAB_ffff00009070b0ec;
      }
      iVar2 = FUN_ffff00009077ede4(puVar7[1],param_1);
    } while (iVar2 != 0);
    iVar2 = FUN_ffff00009077ede4(s_partition_size_userdata_ffff0000907e40c5,param_1);
    if ((iVar2 != 0) ||
       (iVar2 = FUN_ffff00009077ede4(s_rodin_ffff0000907d72a8,&DAT_ffff0000907d72a3), iVar2 != 0)) {
      puVar4 = (undefined *)puVar7[2];
LAB_ffff00009070b0ec:
      FUN_ffff000090709d50(puVar4);
      return;
    }
    lVar5 = FUN_ffff00009077db58(puVar7[2],0,0x10);
    iVar2 = FUN_ffff000090794608();
    local_68 = 0;
    local_78 = 0;
    uStack_70 = 0;
    uVar3 = FUN_ffff00009077f434
                      (&local_78,0x11,&DAT_ffff0000907f5ae7,lVar5 + (long)iVar2 * -0x40000000);
    if (uVar3 < 0x11) {
      pcVar6 = (char *)&local_78;
    }
    else {
      FUN_ffff00009077de54(s_snprintf_partition_size_error____ffff0000907d0a05,uVar3);
      pcVar6 = (char *)puVar7[2];
    }
  }
  FUN_ffff000090709d50(pcVar6);
  return;
}


