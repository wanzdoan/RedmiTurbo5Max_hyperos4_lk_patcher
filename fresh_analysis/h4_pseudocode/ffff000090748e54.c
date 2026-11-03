
undefined8 FUN_ffff000090748e54(undefined8 param_1,long param_2,undefined4 param_3)

{
  uint uVar1;
  undefined4 *puVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  undefined8 uVar6;
  char *pcVar7;
  ulong uVar8;
  undefined8 local_70;
  long local_68;
  undefined4 *local_60;
  long lStack_58;
  
  local_60 = (undefined4 *)0x0;
  lStack_58 = param_2;
  iVar3 = FUN_ffff000090746fd0(param_1,s__soc_connscp_ffff0000907e4def,s__connscp_ffff0000907fbc2d);
  if (iVar3 == -2) {
    FUN_ffff00009077e11c
              (s___s__Fail_to_get_dts_connscp_nod_ffff0000907f680d,
               s_connsys_dfd_setup_ffff0000907f8652);
    uVar6 = 0xfffffffe;
  }
  else {
    iVar4 = FUN_ffff000090747044(param_1,iVar3,s_dfd_value_region_size_ffff0000907fd8ce);
    if (iVar4 == 0) {
      pcVar7 = s___s__Fail_to_get_dfd_value_regio_ffff0000907fd8e4;
    }
    else {
      local_70 = 0;
      FUN_ffff00009077e11c
                (s___s__region_size___x__ffff0000907e980c,s_connsys_dfd_setup_ffff0000907f8652,iVar4
                );
      iVar5 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_AP_RO_CONN_RO_ffff0000907e4de1,param_3,&local_60
                         ,0xc,param_2,0,0);
      if (iVar5 == 0) {
        FUN_ffff00009077e11c
                  (s___s__pa___lX__conninfra_dfd_dump_ffff0000907d64d6,
                   s_connsys_dfd_setup_ffff0000907f8652,param_2,0x33e0,0x1230,0x1020,0x40,local_60);
        puVar2 = local_60;
        *(undefined8 *)(local_60 + 1) = 0x4020240620;
        FUN_ffff00009077e11c
                  (s___s__conninfra_size___lu__ffff0000907ff4b0,s_connsys_dfd_setup_ffff0000907f8652
                   ,0x33e0);
        puVar2[3] = 0x33e;
        FUN_ffff00009077eb20((long)puVar2 + (ulong)(uint)puVar2[2],&DAT_ffff000090806308,0x33e0);
        FUN_ffff00009077e11c
                  (s___s__wifi_size___lu__ffff0000907ee53b,s_connsys_dfd_setup_ffff0000907f8652,
                   0x1230);
        puVar2[6] = 0x123;
        *(undefined8 *)(puVar2 + 4) = 0x342020240620;
        FUN_ffff00009077eb20(puVar2 + 0xd08,&DAT_ffff00009080a708,0x1230);
        FUN_ffff00009077e11c
                  (s___s__bt_size___lu__ffff000090800f2f,s_connsys_dfd_setup_ffff0000907f8652,0x1020
                  );
        puVar2[9] = 0x102;
        *(undefined8 *)(puVar2 + 7) = 0x465020240620;
        FUN_ffff00009077eb20(puVar2 + 0x1194,&DAT_ffff0000908096e8,0x1020);
        uVar1 = iVar4 * 3;
        uVar8 = (ulong)uVar1;
        puVar2[10] = uVar1;
        puVar2[0xb] = iVar4;
        puVar2[0xc] = iVar4 * 4;
        puVar2[0xd] = iVar4;
        puVar2[0xe] = iVar4 * 5;
        puVar2[0xf] = iVar4;
        FUN_ffff00009077ee50((long)puVar2 + uVar8,0,uVar8);
        param_2 = (ulong)(uint)puVar2[10] + param_2;
        local_68 = param_2;
        FUN_ffff00009077e11c
                  (s___s__v___x__cmd___x___x____x___x_ffff0000907ff4cc,
                   s_connsys_dfd_setup_ffff0000907f8652,*puVar2,puVar2[2],puVar2[3],puVar2[5],
                   puVar2[9],puVar2[8],puVar2[9],puVar2[10],puVar2[0xb],puVar2[0xc],puVar2[0xd],
                   puVar2[0xe],puVar2[0xf]);
        FUN_ffff00009077e11c(s___s__copy_done_ffff0000907f6840,s_connsys_dfd_setup_ffff0000907f8652)
        ;
        FUN_ffff000090707c70(local_60,param_3);
        FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_60);
        FUN_ffff00009077e11c
                  (s___s__value_pa_size___lx___x__ffff0000907e3198,
                   s_connsys_dfd_setup_ffff0000907f8652,param_2,iVar4);
        FUN_ffff0000907470a0
                  (param_1,iVar3,s_dfd_cmd_addr_ffff0000907ff517,s_dfd_cmd_addr_ffff0000907ff517,
                   &lStack_58,&local_70);
        FUN_ffff000090747118
                  (param_1,iVar3,s_dfd_cmd_size_ffff0000907e65dc,s_dfd_cmd_size_ffff0000907e65dc,
                   uVar1);
        FUN_ffff0000907470a0
                  (param_1,iVar3,s_dfd_value_addr_ffff0000907e1877,s_dfd_value_addr_ffff0000907e1877
                   ,&local_68,&local_70);
        FUN_ffff000090747118
                  (param_1,iVar3,s_dfd_value_size_ffff0000907e65e9,s_dfd_value_size_ffff0000907e65e9
                   ,uVar8);
        return 0;
      }
      pcVar7 = s___s__CONN_RO_vmm_alloc_failed_ffff0000907d64a7;
    }
    FUN_ffff00009077e11c(pcVar7,s_connsys_dfd_setup_ffff0000907f8652);
    uVar6 = 0xffffffff;
  }
  return uVar6;
}


