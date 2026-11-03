// ffff000090760710  size=616  FUN_ffff000090760710

long FUN_ffff000090760710(long param_1,ulong param_2,uint *param_3,int param_4)

{
  char cVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  char cVar5;
  long lVar6;
  uint local_68;
  int local_64;
  
  local_64 = 0;
  local_68 = 0;
  if (((0xb < param_2) && (param_1 != 0)) && (param_3 != (uint *)0x0)) {
    lVar6 = 0;
    cVar5 = '\0';
    do {
      FUN_ffff00009077eb20(&local_64,param_1 + lVar6,4);
      if ((local_64 == 0x43474244) &&
         (FUN_ffff00009077eb20(&local_68,param_1 + lVar6 + 8,4), local_68 != 0)) {
        if (local_68 == 0xffffffff) {
          pcVar3 = s__s__d__the_value_of_last_kmsg_pm_ffff0000907e691f;
          uVar4 = 0x107;
        }
        else if (local_68 < 0x200001) {
          cVar1 = cVar5 + '\x01';
          if ((param_4 == 1) && (cVar1 != '\x02')) {
            FUN_ffff00009077e11c
                      (s__s__d__search_the_begin_of_last_p_ffff0000907e9ceb,
                       s_search_and_copy_data_ffff0000907db535,0x110);
            cVar5 = cVar1;
            goto LAB_ffff000090760784;
          }
          if (cVar5 == '\0') {
            FUN_ffff00009077e11c
                      (s__s__d__hit_the_begin_of_last_kms_ffff0000907fa5d4,
                       s_search_and_copy_data_ffff0000907db535,0x114);
            pcVar3 = s__s__d__the_size_of_last_kmsg_is__ffff0000907d3574;
            uVar4 = 0x115;
LAB_ffff000090760874:
            FUN_ffff00009077e11c(pcVar3,s_search_and_copy_data_ffff0000907db535,uVar4,local_68);
          }
          else if (cVar1 == '\x02') {
            FUN_ffff00009077e11c
                      (s__s__d__hit_the_begin_of_last_pms_ffff000090801380,
                       s_search_and_copy_data_ffff0000907db535,0x117);
            pcVar3 = s__s__d__the_size_of_last_pmsg_is__ffff0000907e8229;
            uVar4 = 0x118;
            goto LAB_ffff000090760874;
          }
          *param_3 = local_68;
          cVar5 = cVar1;
          if (param_2 < lVar6 + (ulong)local_68 + 0xc) goto LAB_ffff000090760784;
          lVar2 = FUN_ffff00009077d514(local_68 + 1);
          if (lVar2 == 0) {
            pcVar3 = s__s__d__Memory_allocation_failed__ffff0000907d81fc;
            uVar4 = 0x122;
          }
          else {
            if ((ulong)local_68 + lVar6 + 0xc <= param_2) {
              FUN_ffff00009077eb20(lVar2,param_1 + lVar6 + 0xc);
              *(undefined1 *)(lVar2 + (ulong)local_68) = 0;
              FUN_ffff00009077e11c
                        (s__s__d__last_kmsg_buffer_allocate_ffff0000907fa5f8,
                         s_search_and_copy_data_ffff0000907db535,0x12d,lVar2);
              return lVar2;
            }
            pcVar3 = s__s__d__the_size_of_data_to_copy_e_ffff0000907f3457;
            uVar4 = 0x12a;
          }
        }
        else {
          pcVar3 = s__s__d__the_value_of_last_kmsg_pm_ffff0000907f02f1;
          uVar4 = 0x10a;
        }
        FUN_ffff00009077e11c(pcVar3,s_search_and_copy_data_ffff0000907db535,uVar4);
        break;
      }
LAB_ffff000090760784:
      lVar6 = lVar6 + 1;
    } while (param_2 - 0xb != lVar6);
    *param_3 = 0;
  }
  return 0;
}


