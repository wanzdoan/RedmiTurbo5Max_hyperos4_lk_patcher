// ffff0000907353a0  size=600  FUN_ffff0000907353a0

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000907353a0(long param_1,long param_2,undefined1 param_3)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  undefined8 *puVar4;
  long lVar5;
  char *pcVar6;
  undefined8 uVar7;
  long local_a0 [10];
  long local_38;
  
  local_38 = 0;
  local_a0[0] = 0;
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff00009077de54(s__Part_WP_Input_name_is_NULL_ffff0000907dab25);
    return -0x16;
  }
  iVar2 = FUN_ffff00009077f080(param_1,s_preloader_ffff0000907d73dd,10);
  if (iVar2 == 0) {
    local_a0[8] = 0;
    local_a0[9] = 0;
    local_a0[6] = 0;
    local_a0[7] = 0;
    local_a0[4] = 0;
    local_a0[5] = 0;
    local_a0[2] = 0;
    local_a0[3] = 0;
    local_a0[1] = 0;
    puVar4 = (undefined8 *)FUN_ffff00009077d294(0x40,0x10);
    if (puVar4 == (undefined8 *)0x0) goto LAB_ffff000090735564;
    iVar2 = FUN_ffff0000907352bc(local_a0 + 1,0x48);
    if (iVar2 != 0) goto LAB_ffff0000907355cc;
    lVar5 = FUN_ffff00009077bc50(local_a0 + 1);
    if (lVar5 == 0) {
      pcVar6 = (char *)(local_a0 + 1);
      goto LAB_ffff0000907355b4;
    }
    *puVar4 = 0;
    uVar1 = *(undefined4 *)(lVar5 + 0x38);
    *(undefined1 *)((long)puVar4 + 0xc) = param_3;
    *(undefined4 *)(puVar4 + 1) = uVar1;
    FUN_ffff00009077de54
              (s__Part_WP___s___lu__u__u_ffff0000907dab61,*(undefined8 *)(lVar5 + 0x18),0,uVar1,
               param_3);
    iVar2 = FUN_ffff00009077bf48(lVar5,0x101,puVar4);
    if (iVar2 == 0) {
      iVar2 = 0;
    }
    else {
      FUN_ffff00009077de54(s__Part_WP__bio_set_fialed_ffff0000907ec456);
      iVar2 = -0x1c;
    }
  }
  else {
    puVar4 = (undefined8 *)FUN_ffff00009077d294(0x40,0x10);
    if (puVar4 == (undefined8 *)0x0) {
LAB_ffff000090735564:
      FUN_ffff00009077de54(s__Part_WP_Lack_of_memory____ffff0000907f131e);
      return -0x1c;
    }
    iVar2 = FUN_ffff000090735620(param_1,&local_38);
    iVar3 = FUN_ffff000090735620(param_2,local_a0);
    if (((iVar3 != 0 || iVar2 != 0) || (local_38 == 0)) || (local_a0[0] == 0)) {
      FUN_ffff00009077de54(s__Part_WP_no_find_start__s_end__s_ffff0000907f133a,param_1,param_2);
      iVar2 = -0x16;
      goto LAB_ffff0000907355cc;
    }
    uVar7 = *(undefined8 *)(local_38 + 0x20);
    iVar2 = *(int *)(local_a0[0] + 0x28);
    *(undefined1 *)((long)puVar4 + 0xc) = param_3;
    *puVar4 = uVar7;
    iVar2 = (iVar2 - (int)uVar7) + 1;
    *(int *)(puVar4 + 1) = iVar2;
    lVar5 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
    if (lVar5 == 0) {
      pcVar6 = s_bootdevice_ffff0000907d0ccc;
LAB_ffff0000907355b4:
      FUN_ffff00009077de54(s_Partition___s__is_not_exist__ffff0000907ea8d7,pcVar6);
      iVar2 = -0x13;
      goto LAB_ffff0000907355cc;
    }
    FUN_ffff00009077de54
              (s__Part_WP___s__>__s___lu__u__u_ffff0000907dab42,param_1,param_2,uVar7,iVar2,param_3)
    ;
    iVar2 = FUN_ffff00009077bf48(lVar5,0x101,puVar4);
    if (iVar2 != 0) {
      FUN_ffff00009077de54(s__Part_WP__bio_set_fialed_ffff0000907ec456);
    }
  }
  FUN_ffff00009077bce4(lVar5);
LAB_ffff0000907355cc:
  thunk_FUN_ffff0000907c2ad8(puVar4);
  return iVar2;
}


