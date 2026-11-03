
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint ccci_dump_write(uint param_1,uint param_2,char *param_3)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  ulong uVar4;
  code *pcVar5;
  uint uVar6;
  int iVar7;
  void *__src;
  ulong uVar8;
  undefined8 uVar9;
  void *__dest;
  void *__src_00;
  size_t sVar10;
  long lVar11;
  undefined4 uVar12;
  uint uVar13;
  size_t __n;
  long *plVar14;
  undefined8 in_stack_fffffffffffffee0;
  undefined4 uVar15;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined4 local_a0;
  long local_98;
  undefined8 local_90;
  
  uVar15 = (undefined4)((ulong)in_stack_fffffffffffffee0 >> 0x20);
  lVar11 = sp_el0;
  lVar11 = *(long *)(lVar11 + 0x620);
  local_98 = 0;
  local_90 = 0;
  if (param_1 < 9) {
    if ((&DAT_00113460)[(ulong)param_1 * 6] == param_1) {
      local_c0 = 0;
      local_b8 = 0;
      local_b0 = 0;
      uStack_a8 = 0;
      local_a0 = 0;
      if (9 < param_1) {
                    /* WARNING: Does not return */
        pcVar5 = (code *)SoftwareBreakpoint(1,0x104a7c);
        (*pcVar5)();
      }
      plVar14 = (long *)(&node_array)[(ulong)param_1 * 3];
      if (*plVar14 == 0) {
        uVar6 = 0xfffffffb;
      }
      else if (((*(byte *)(plVar14 + 3) >> 1 & 1) == 0) && ((int)plVar14[2] == (int)plVar14[1])) {
        uVar6 = 0xfffffffa;
      }
      else {
        __src = (void *)kmalloc_trace(_kobject_init_and_add,0x820,0x200);
        if (__src == (void *)0x0) {
          uVar6 = 0xfffffff9;
        }
        else {
          if ((param_2 & 1) == 0) {
            uVar6 = 0;
          }
          else {
            uVar4 = daif;
            uVar8 = sched_clock();
            if ((param_2 >> 3 & 1) == 0) {
              lVar3 = sp_el0;
              *(int *)(lVar3 + 0x10) = *(int *)(lVar3 + 0x10) + 1;
              lVar2 = tpidr_el1;
              uVar15 = *(undefined4 *)(&cpu_number + lVar2);
              lVar2 = *(long *)(lVar3 + 0x10) + -1;
              *(int *)(lVar3 + 0x10) = (int)lVar2;
              if ((lVar2 == 0) || (*(long *)(lVar3 + 0x10) == 0)) {
                preempt_schedule();
              }
              uVar12 = 0x20;
              if ((uVar4 & 0x80) != 0) {
                uVar12 = 0x2d;
              }
              uVar6 = scnprintf(__src,0x200,"[%5lu.%06lu]%c(%x)[%d:%s]",uVar8 / 1000000000,
                                (uVar8 % 1000000000) / 1000,uVar12,uVar15,
                                *(undefined4 *)(lVar3 + 0x618),lVar3 + 0x830);
            }
            else {
              ktime_get_real_ts64(&local_98);
              local_98 = local_98 + (long)_sys_tz * -0x3c;
              rtc_time64_to_tm(local_98,&local_c0);
              uVar6 = scnprintf(__src,0x200,"[%d-%02d-%02d %02d:%02d:%02d.%03d]",
                                local_b0._4_4_ + 0x76c,(int)local_b0 + 1,local_b8._4_4_,
                                local_b8 & 0xffffffff,local_c0._4_4_,
                                CONCAT44(uVar15,(undefined4)local_c0),(uint)local_90 / 1000);
              iVar7 = scnprintf((long)__src + (ulong)uVar6,0x200 - uVar6,"[%5lu.%06lu]",
                                uVar8 / 1000000000,(uVar8 % 1000000000) / 1000);
              uVar6 = iVar7 + uVar6;
            }
          }
          iVar7 = vsnprintf((char *)((long)__src + (ulong)uVar6),(ulong)(0x200 - uVar6),param_3,
                            &stack0xffffffffffffff20);
          uVar6 = iVar7 + uVar6;
          if (0x1ff < uVar6) {
            _printk(&DAT_0011135c,"ccci_dump_write",0x1d6,uVar6,0x200);
            uVar6 = 0x1ff;
          }
          uVar9 = _raw_spin_lock_irqsave((undefined *)((long)plVar14 + 0x1c));
          if ((param_2 >> 1 & 1) != 0) {
            *(undefined8 *)((long)plVar14 + 0xc) = 0;
          }
          iVar7 = (int)plVar14[2];
          uVar13 = *(uint *)(plVar14 + 1);
          if ((*(byte *)(plVar14 + 3) >> 1 & 1) == 0) {
            if (uVar13 < iVar7 + uVar6) {
              uVar13 = uVar13 - iVar7;
              if (0x200 < uVar13) goto LAB_00104a80;
              sVar10 = (size_t)(int)uVar13;
            }
            else {
              sVar10 = (size_t)uVar6;
              uVar13 = uVar6;
            }
            memcpy((void *)(*plVar14 + (ulong)*(uint *)(plVar14 + 2)),__src,sVar10);
            uVar13 = (int)plVar14[2] + uVar13;
          }
          else {
            if (uVar13 < iVar7 + uVar6) {
              uVar13 = uVar13 - iVar7;
              if (0x200 < uVar13) {
LAB_00104a80:
                    /* WARNING: Subroutine does not return */
                fortify_panic("memcpy");
              }
              __n = (size_t)(int)uVar13;
              memcpy((void *)(*plVar14 + (ulong)*(uint *)(plVar14 + 2)),__src,__n);
              sVar10 = (size_t)(uVar6 - uVar13);
              if (0x200 - __n < sVar10) goto LAB_00104a80;
              __dest = (void *)*plVar14;
              __src_00 = (void *)((long)__src + __n);
            }
            else {
              sVar10 = (size_t)uVar6;
              __dest = (void *)(*plVar14 + (ulong)*(uint *)(plVar14 + 2));
              __src_00 = __src;
            }
            memcpy(__dest,__src_00,sVar10);
            uVar1 = *(uint *)(plVar14 + 1);
            uVar13 = *(int *)((long)plVar14 + 0xc) + uVar6;
            if (uVar1 < uVar13) {
              uVar13 = uVar1 + 1;
            }
            *(uint *)((long)plVar14 + 0xc) = uVar13;
            uVar13 = (int)plVar14[2] + uVar6 & uVar1 - 1;
          }
          *(uint *)(plVar14 + 2) = uVar13;
          if (*(uint *)((long)plVar14 + 0x14) < uVar13) {
            *(uint *)((long)plVar14 + 0x14) = uVar13;
          }
          _raw_spin_unlock_irqrestore((undefined *)((long)plVar14 + 0x1c),uVar9);
          kfree(__src);
        }
      }
    }
    else {
      uVar6 = 0xfffffffc;
    }
  }
  else {
    uVar6 = 0xfffffffe;
  }
  lVar2 = sp_el0;
  if (*(long *)(lVar2 + 0x620) == lVar11) {
    return uVar6;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}

